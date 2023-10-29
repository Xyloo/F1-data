package IntegrationTests;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.Data.Circuit;
import pl.pollub.f1data.Repositories.F1Database.CircuitRepository;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes=pl.pollub.f1data.F1DataApplication.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CircuitControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CircuitRepository circuitRepository;
    @Autowired
    private JacksonTester<CircuitSummaryDto> jsonCircuit;

    @Nested
    public class ValidationTests {
        @Test
        public void shouldReturnNotFoundForInvalidId() throws Exception {
            //given
            int invalidId = circuitRepository.findAll().stream().mapToInt(Circuit::getId).max().orElse(0) + 1;
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/circuits/" + invalidId)
            ).andReturn().getResponse();
            //then
            assertThat(response.getStatus(), equalTo(404));
        }

        @Test
        public void shouldReturnNotFoundForNegativeId() throws Exception {
            //given
            int invalidId = -1;
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/circuits/" + invalidId)
            ).andReturn().getResponse();
            //then
            assertThat(response.getStatus(), equalTo(404));
        }

        @Test
        public void shouldReturnBadRequestForStringId () throws Exception {
            //given
            String invalidId = "invalidId";
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/circuits/" + invalidId)
            ).andReturn().getResponse();
            //then
            assertThat(response.getStatus(), equalTo(400));
        }
    }

    @Nested
    public class AnonymousUserTests {
        @Test
        public void shouldReturnMaxThreeMostRecentRaces() throws Exception {
            //given
            Circuit circuit = circuitRepository.findAll().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/circuits/" + circuit.getId())
            ).andReturn().getResponse();
            CircuitSummaryDto parsedCircuit = jsonCircuit.parse(response.getContentAsString()).getObject();
            //then
            assertThat(response.getStatus(), equalTo(200));
            assertThat(parsedCircuit.getRaces().size(), lessThanOrEqualTo(3));
        }

        @Test
        public void shouldReturnForbiddenOnDeleteEndpoints() throws Exception {
            //given
            Circuit circuit = circuitRepository.findAll().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    delete("/api/circuits/" + circuit.getId())
            ).andReturn().getResponse();
            //then
            assertThat(response.getStatus(), equalTo(403));
        }
        @Test
        public void shouldReturnOKOrNotFoundForEveryCircuit() throws Exception {
            //given
            List<Circuit> circuits = circuitRepository.findAll();
            //when
            for (Circuit c : circuits) {
                MockHttpServletResponse response = mockMvc.perform(
                        get("/api/circuits/" + c.getId())
                ).andReturn().getResponse();
                //then
                assertThat(response.getStatus(), either(equalTo(200)).or(equalTo(404)));
            }
        }

        @Test
        public void shouldReturnAllCircuits() throws Exception {
            //given
            List<Circuit> circuits = circuitRepository.findAll();
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/circuits/")
            ).andReturn().getResponse();
            ObjectMapper mapper = new ObjectMapper();
            mapper.addHandler(new DeserializationProblemHandler() {
                @Override
                public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser p, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
                    if(propertyName.equals("lat") || propertyName.equals("lng") || propertyName.equals("alt") || propertyName.equals("url")) {
                        p.skipChildren();
                        return true;
                    }
                    return false;
                }
            });
            List<CircuitSummaryDto> parsedCircuits = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CircuitSummaryDto.class));
            //then
            assertThat(response.getStatus(), equalTo(200));
            assertThat(parsedCircuits.size(), equalTo(circuits.size()));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class LoggedInUserTests {
        private String token = "";
        @BeforeAll
        public void setup() {
            token = login("testUser", "user");
        }
        @Test
        @Transactional
        public void shouldReturnAllRacesOnCircuitDetails() throws Exception {
            //given
            Circuit circuit = circuitRepository.findAll().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/circuits/" + circuit.getId())
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            CircuitSummaryDto parsedCircuit = jsonCircuit.parse(response.getContentAsString()).getObject();
            //then
            assertThat(response.getStatus(), equalTo(200));
            assertThat(parsedCircuit.getRaces().size(), equalTo(circuit.getRaces().size()));
        }
        @Test
        public void shouldReturnForbiddenOnDeleteEndpoints() throws Exception {
            //given
            Circuit circuit = circuitRepository.findAll().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    delete("/api/circuits/" + circuit.getId())
            ).andReturn().getResponse();
            //then
            assertThat(response.getStatus(), equalTo(403));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class AdminTests {
        private String token = "";
        @BeforeAll
        public void setup() {
            token = login("testAdmin", "admin");
        }
        @Test
        public void shouldReturnOKOrNotFoundOnDeleteEndpoints() throws Exception {
            //given
            //Circuit circuit = circuitRepository.findAll().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    delete("/api/circuits/" + 9999999)
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            //then
            assertThat(response.getStatus(), either(equalTo(200)).or(equalTo(404)));
        }

    }

    private String login(String username, String password) {
        String token = "";
        try {
            String response = mockMvc.perform(
                    post("/api/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"password\": \"" + password + "\", \"username\": \"" + username + "\"}")
            ).andReturn().getResponse().getContentAsString();
            token = response.substring(response.indexOf(':') + 2, response.indexOf(',') - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

}
