package IntegrationTests;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.Data.Circuit;
import pl.pollub.f1data.Repositories.F1Database.CircuitRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
    }
}
