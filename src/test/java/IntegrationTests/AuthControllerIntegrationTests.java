package IntegrationTests;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Repositories.RoleRepository;
import pl.pollub.f1data.Repositories.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes=pl.pollub.f1data.F1DataApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    private JacksonTester<User> jsonUser;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, JsonMapper.builder().disable(MapperFeature.USE_ANNOTATIONS).build());
    }

    @Test
    public void shouldReturnJwtTokenOnSuccessfulLogin() throws Exception {
        //given
        //this user is seeded
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("user");
        assert userRepository.getUserByUsername("testUser").get().isPresent();
        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser.write(user).getJson())
        ).andReturn().getResponse();
        // then
        assert response.getStatus() == 200;
        assert response.getContentAsString().contains("token");
    }

    @Test
    public void shouldReturnErrorMessageWhenUserDataIncorrect() throws Exception {
        //given
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("wrongPassword");
        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser.write(user).getJson())
        ).andReturn().getResponse();
        // then
        assert response.getStatus() == 401;
        assert response.getContentAsString().contains("Error: Username and/or password are incorrect.");
    }

}
