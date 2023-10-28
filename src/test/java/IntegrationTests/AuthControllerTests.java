package IntegrationTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import pl.pollub.f1data.Repositories.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes=pl.pollub.f1data.F1DataApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private JacksonTester<User> jsonUser;

    @BeforeEach
    public void setup() {
        //I couldn't find a better workaround to bypass the @JsonIgnore annotation on password field
        //which has to be done so that the password is serialized to JSON
        JacksonTester.initFields(this, JsonMapper.builder().disable(MapperFeature.USE_ANNOTATIONS).build());
    }

    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            randomString.append(chars.charAt(randomIndex));
        }
        return randomString.toString();
    }

    @Nested
    public class LoginTests {
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
        public void shouldReturnErrorMessageWhenPasswordIncorrect() throws Exception {
            //given
            User user = new User();
            user.setUsername("testUser");
            user.setPassword("admin");
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

    @Nested
    public class RegisterTests {
        @Test
        public void shouldReturnSuccessMessageWhenUserRegistered() throws Exception {
            //given
            User user = new User();
            user.setUsername(generateRandomString(10));
            user.setPassword("useruser");
            user.setEmail(generateRandomString(10) + "@testing.com");
            while (userRepository.getUserByUsername(user.getUsername()).get().isPresent() || userRepository.getUserByEmail(user.getEmail()).get().isPresent()) {
                user.setUsername(generateRandomString(10));
                user.setEmail(generateRandomString(10) + "@testing.com");
            }
            assert userRepository.getUserByUsername(user.getUsername()).get().isEmpty();
            // when
            MockHttpServletResponse response = mockMvc.perform(
                    post("/api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonUser.write(user).getJson())
            ).andReturn().getResponse();
            // then
            assert response.getStatus() == 201;
            assert response.getContentAsString().contains("User registered successfully!");

            //cleanup
            userRepository.delete(userRepository.getUserByUsername(user.getUsername()).get().get());
        }

        @Nested
        public class UserExistsAlreadyTests {
            @Test
            public void shouldReturnErrorMessageWhenUsernameAlreadyExists() throws Exception {
                //given
                User user = new User();
                user.setUsername("testUser");
                user.setPassword("useruser");
                user.setEmail("user@testing.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 409;
                assert response.getContentAsString().contains("Error: Username already exists");
            }

            @Test
            public void shouldReturnErrorMessageWhenEmailAlreadyExists() throws Exception {
                //given
                User user = new User();
                user.setUsername("testUser2");
                user.setPassword("useruser");
                user.setEmail("user@f1-data.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 409;
                assert response.getContentAsString().contains("Error: Email already exists");
            }
        }

        @Nested
        public class FieldsNotSetTests {
            @Test
            public void shouldReturnErrorMessageWhenUsernameNotSet() throws Exception {
                //given
                User user = new User();
                user.setPassword("useruser");
                user.setEmail(generateRandomString(10) + "@testing.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assertThat(response.getContentAsString(), either(
                        containsString("Error: Username must be between 3 and 32 characters long.")).or(
                        containsString("Error: Username cannot be blank."))
                );
            }

            @Test
            public void shouldReturnErrorMessageWhenPasswordNotSet() throws Exception {
                //given
                User user = new User();
                user.setUsername(generateRandomString(10));
                user.setEmail(generateRandomString(10) + "@testing.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assertThat(response.getContentAsString(), either(
                        containsString("Error: Password must be between 6 and 32 characters long.")).or(
                        containsString("Error: Password cannot be blank."))
                );
            }

            @Test
            public void shouldReturnErrorMessageWhenEmailNotSet() throws Exception {
                //given
                User user = new User();
                user.setUsername(generateRandomString(10));
                user.setPassword("useruser");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assertThat(response.getContentAsString(), either(
                        containsString("Error: Email cannot be blank.")).or(
                        containsString("Error: Email must be valid."))
                );
            }
        }
        @Nested
        public class DataInvalidTests {
            @Test
            public void shouldReturnErrorMessageWhenUsernameTooShort() throws Exception {
                //given
                User user = new User();
                user.setUsername("t");
                user.setPassword("useruser");
                user.setEmail(generateRandomString(10) + "@testing.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assert response.getContentAsString().contains("Error: Username must be between 3 and 32 characters long.");
            }

            @Test
            public void shouldReturnErrorMessageWhenUsernameTooLong() throws Exception {
                //given
                User user = new User();
                user.setUsername(generateRandomString(35));
                user.setPassword("useruser");
                user.setEmail(generateRandomString(10) + "@testing.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assert response.getContentAsString().contains("Error: Username must be between 3 and 32 characters long.");
            }

            @Test
            public void shouldReturnErrorMessageWhenEmailInvalid() throws Exception {
                //given
                User user = new User();
                user.setUsername(generateRandomString(10));
                user.setPassword("useruser");
                user.setEmail("invalidEmail");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assert response.getContentAsString().contains("Error: Email must be valid.");
            }
            @Test
            public void shouldReturnErrorMessageWhenEmailTooLong() throws Exception {
                //given
                User user = new User();
                user.setUsername(generateRandomString(10));
                user.setPassword("useruser");
                user.setEmail(generateRandomString(100) + "@testing.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assertThat(response.getContentAsString(), either(
                        containsString("Error: Email must be less than 100 characters long.")).or(
                        containsString("Error: Email must be valid."))
                );
            }

            @Test
            public void shouldReturnErrorMessageWhenPasswordTooLong() throws Exception {
                //given
                User user = new User();
                user.setUsername(generateRandomString(10));
                user.setPassword(generateRandomString(110));
                user.setEmail(generateRandomString(15) + "@testing.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assert response.getContentAsString().contains("Error: Password must be between 6 and 100 characters long.");
            }
        }
        @Nested
        public class DataBlankTests {
            @Test
            public void shouldReturnErrorMessageWhenUsernameBlank() throws Exception {
                //given
                User user = new User();
                user.setUsername("");
                user.setPassword("useruser");
                user.setEmail(generateRandomString(10) + "@testing.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assertThat(response.getContentAsString(), either(
                        containsString("Error: Username must be between 3 and 32 characters long.")).or(
                        containsString("Error: Username cannot be blank."))
                );
            }

            @Test
            public void shouldReturnErrorMessageWhenEmailBlank() throws Exception {
                //given
                User user = new User();
                user.setUsername(generateRandomString(10));
                user.setPassword("useruser");
                user.setEmail("");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user)
                                        .getJson())).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assertThat(response.getContentAsString(), either(
                        containsString("Error: Email cannot be blank.")).or(
                        containsString("Error: Email must be valid."))
                );

            }

            @Test
            public void shouldReturnErrorMessageWhenPasswordBlank() throws Exception {
                //given
                User user = new User();
                user.setUsername(generateRandomString(10));
                user.setPassword("");
                user.setEmail(generateRandomString(10) + "@testing.com");
                // when
                MockHttpServletResponse response = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser
                                        .write(user)
                                        .getJson())).andReturn().getResponse();
                // then
                assert response.getStatus() == 400;
                assertThat(response.getContentAsString(), either(
                        containsString("Error: Password must be between 6 and 100 characters long.")).or(
                        containsString("Error: Password cannot be blank."))
                );
            }
        }
    }

    @Nested
    public class LogoutTests {
        @Test
        public void shouldReturnErrorMessageWhenUserNotLoggedIn() throws Exception {
            //given
            // when
            MockHttpServletResponse response = mockMvc.perform(
                    post("/api/auth/logout")
                            .contentType(MediaType.APPLICATION_JSON)
            ).andReturn().getResponse();
            // then
            assert response.getStatus() == 401;
            assert response.getContentAsString().contains("Error: User is not logged in!");
        }

        @Test
        public void shouldReturnSuccessMessageWhenUserLoggedOut() throws Exception {
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
            assert response.getStatus() == 200;
            assert response.getContentAsString().contains("token");
            JsonNode jsonNode = JsonMapper.builder().build().readTree(response.getContentAsString());
            String token = jsonNode.get("token").asText();
            // when
            response = mockMvc.perform(
                    post("/api/auth/logout")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            // then
            assert response.getStatus() == 200;
            assert response.getContentAsString().contains("User logged out successfully!");
        }
    }
}
