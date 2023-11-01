package IntegrationTests;

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
import pl.pollub.f1data.Models.DTOs.ConstructorDto;
import pl.pollub.f1data.Repositories.F1Database.ConstructorRepository;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes=pl.pollub.f1data.F1DataApplication.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConstructorControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ConstructorRepository constructorRepository;
    @Autowired
    private JacksonTester<List<ConstructorDto>> jsonConstructors;

    @Nested
    public class ValidationTests {
        @Test
        public void shouldReturnNotFoundForInvalidId() throws Exception {
            //given
            int invalidId = constructorRepository.findAllConstructors().stream().mapToInt(ConstructorDto::getId)
                    .max().orElse(0) + 1;
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/constructor/" + invalidId)
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
                    get("/api/constructor/" + invalidId)
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
                    get("/api/constructor/" + invalidId)
            ).andReturn().getResponse();
            //then
            assertThat(response.getStatus(), equalTo(400));
        }
    }

    @Nested
    public class AnonymousUserTests{

        @Test
        public void getAllConstructorsByNationalityShouldReturnBritishConstructors() throws Exception {
            //given
            String nationality = "British";
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/constructor/nationality/" + nationality)
            ).andReturn().getResponse();

            List<ConstructorDto> constructors = jsonConstructors.parse(response.getContentAsString()).getObject();
            //then
            assertThat(constructors.isEmpty(), is(false));
            assertThat(constructors.stream().allMatch(constructorDto -> "British"
                    .equals(constructorDto.getNationality())), is(true));

        }

        @Test
        public void shouldReturnForbiddenOnDeleteEndpoints() throws Exception {
            //given
            ConstructorDto constructor = constructorRepository.findAllConstructors().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    delete("/api/constructor/" + constructor.getId())
            ).andReturn().getResponse();
            //then
            assertThat(response.getStatus(), equalTo(403));
        }

        @Test
        public void shouldReturnOKOrNotFoundForEveryConstructor() throws Exception {
            //given
            List<ConstructorDto> constructors = constructorRepository.findAllConstructors();
            //when
            for (ConstructorDto c : constructors) {
                MockHttpServletResponse response = mockMvc.perform(
                        get("/api/constructor/" + c.getId())
                ).andReturn().getResponse();
                //then
                assertThat(response.getStatus(), either(equalTo(200)).or(equalTo(404)));
            }
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
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    delete("/api/constructor/" + 9999999)
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
