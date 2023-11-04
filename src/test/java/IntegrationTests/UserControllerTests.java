package IntegrationTests;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
import pl.pollub.f1data.Models.ERole;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Repositories.RoleRepository;
import pl.pollub.f1data.Repositories.UserRepository;

import java.util.HashSet;
import java.util.Objects;

import static IntegrationTests.AuthControllerTests.generateRandomString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes=pl.pollub.f1data.F1DataApplication.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JacksonTester<User> jsonUser;
    @Nested
    public class ValidationTests {
        @Test
        public void shouldReturnNotFoundForInvalidId() throws Exception {
            //given
            long invalidId = userRepository.findAll().stream().mapToLong(User::getId).max().orElse(-1) + 1;
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/users/" + invalidId)
            ).andReturn().getResponse();
            //then
            assert response.getStatus() == 404;
        }

        @Test
        public void shouldReturnNotFoundForInvalidUsername() throws Exception {
            //given
            String invalidUsername = generateRandomString(10);
            while (userRepository.getUserByUsername(invalidUsername).get().isPresent()) {
                invalidUsername = generateRandomString(10);
            }
            assert userRepository.getUserByUsername(invalidUsername).get().isEmpty();
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/users/" + invalidUsername)
            ).andReturn().getResponse();
            //then
            assert response.getStatus() == 404;
        }

        @Test
        public void shouldReturnNotFoundForNegativeId() throws Exception {
            //given
            long invalidId = -1;
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/users/" + invalidId)
            ).andReturn().getResponse();
            //then
            assert response.getStatus() == 404;
        }
    }

    @Nested
    public class AnonymousUserTests {
        @Test
        public void shouldReturnUserWithEmailFieldBlankedOut() throws Exception {
            //given
            User user = userRepository.findAll().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/user/" + user.getId())
            ).andReturn().getResponse();
            User parsedUser = jsonUser.parse(response.getContentAsString()).getObject();
            //then
            assert response.getStatus() == 200;
            assert parsedUser.getEmail() == null;
            assert parsedUser.getUsername().equals(user.getUsername());
        }

        @Test
        public void shouldReturnSameUserUsingBothIdAndUsername() throws Exception {
            //given
            User user = userRepository.findAll().get(0);
            //when
            MockHttpServletResponse response1 = mockMvc.perform(
                    get("/api/user/" + user.getId())
            ).andReturn().getResponse();
            MockHttpServletResponse response2 = mockMvc.perform(
                    get("/api/user/" + user.getUsername())
            ).andReturn().getResponse();
            User parsedUser1 = jsonUser.parse(response1.getContentAsString()).getObject();
            User parsedUser2 = jsonUser.parse(response2.getContentAsString()).getObject();
            //then
            assert response1.getStatus() == 200;
            assert response2.getStatus() == 200;
            assert parsedUser1.getId().equals(parsedUser2.getId());
            assert parsedUser1.getUsername().equals(parsedUser2.getUsername());
        }

        @Test
        public void shouldReturnForbiddenOnPutEndpoint() throws Exception {
            //given
            User user = userRepository.findAll().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                        put("/api/user/" + user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
            //then
            assert response.getStatus() == 403;
        }

        @Test
        public void shouldReturnForbiddenOnDeleteEndpoint() throws Exception {
            //given
            User user = userRepository.findAll().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    delete("/api/user/" + user.getId())
            ).andReturn().getResponse();
            //then
            assert response.getStatus() == 403;
        }

        @Test
        public void shouldReturnForbiddenOnGetAllEndpoint() throws Exception {
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/user/")
            ).andReturn().getResponse();
            //then
            assert response.getStatus() == 403;
        }

        @Nested
        public class MeEndpointTests {
            @Test
            public void shouldReturnUnauthorizedOnGetEndpoint() throws Exception {
                //when
                MockHttpServletResponse response = mockMvc.perform(
                        get("/api/user/me")
                ).andReturn().getResponse();
                //then
                assert response.getStatus() == 401;
            }

            @Test
            public void shouldReturnUnauthorizedOnPutEndpoint() throws Exception {
                User user = userRepository.findAll().get(0);
                //when
                MockHttpServletResponse response = mockMvc.perform(
                        put("/api/user/me")
                                .content(jsonUser.write(user).getJson())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn().getResponse();
                //then
                assert response.getStatus() == 401;
            }

            @Test
            public void shouldReturnUnauthorizedOnDeleteEndpoint() throws Exception {
                //when
                MockHttpServletResponse response = mockMvc.perform(
                        delete("/api/user/me")
                ).andReturn().getResponse();
                //then
                assert response.getStatus() == 401;
            }
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
        public void shouldReturnEmailWhenQueryingSelf() throws Exception {
            //given
            User user = userRepository.getUserByUsername("testUser").join().orElse(null);
            assert user != null;
            assert user.getEmail() != null;
            //when
            MockHttpServletResponse response1 = mockMvc.perform(
                    get("/api/user/" + user.getId())
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            MockHttpServletResponse response2 = mockMvc.perform(
                    get("/api/user/" + user.getUsername())
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            MockHttpServletResponse response3 = mockMvc.perform(
                    get("/api/user/me")
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            User parsedUser1 = jsonUser.parse(response1.getContentAsString()).getObject();
            User parsedUser2 = jsonUser.parse(response2.getContentAsString()).getObject();
            User parsedUser3 = jsonUser.parse(response3.getContentAsString()).getObject();
            //then
            assert response1.getStatus() == 200;
            assert response2.getStatus() == 200;
            assert response3.getStatus() == 200;
            assert parsedUser1.getId().equals(parsedUser2.getId());
            assert parsedUser1.getId().equals(parsedUser3.getId());
            assert parsedUser1.getUsername().equals(parsedUser2.getUsername());
            assert parsedUser1.getUsername().equals(parsedUser3.getUsername());
            assert parsedUser1.getEmail() != null;
            assert parsedUser2.getEmail() != null;
            assert parsedUser3.getEmail() != null;
            assert parsedUser1.getEmail().equals(parsedUser2.getEmail());
            assert parsedUser1.getEmail().equals(parsedUser3.getEmail());
        }

        @Test
        public void shouldReturnEmailBlankedOutIfQueryingDifferentUser() throws Exception {
            User user = userRepository.getUserByUsername("testAdmin").join().orElse(null);
            assert user != null;
            assert user.getEmail() != null;
            MockHttpServletResponse response1 = mockMvc.perform(
                    get("/api/user/" + user.getId())
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            MockHttpServletResponse response2 = mockMvc.perform(
                    get("/api/user/" + user.getUsername())
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            User parsedUser1 = jsonUser.parse(response1.getContentAsString()).getObject();
            User parsedUser2 = jsonUser.parse(response2.getContentAsString()).getObject();
            //then
            assert response1.getStatus() == 200;
            assert response2.getStatus() == 200;
            assert parsedUser1.getId().equals(parsedUser2.getId());
            assert parsedUser1.getUsername().equals(parsedUser2.getUsername());
            assert parsedUser1.getEmail() == null;
            assert parsedUser2.getEmail() == null;
        }

        @Test
        public void shouldReturnForbiddenOnGetAllEndpoint() throws Exception {
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/user/")
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            //then
            assert response.getStatus() == 403;
        }

        @Test
        public void shouldReturnForbiddenWhenTryingToUpdateOtherUser() throws Exception {
            //given
            User user = userRepository.getUserByUsername("testAdmin").join().orElse(null);
            assert user != null;
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    put("/api/user/" + user.getId())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonUser.write(user).getJson())
            ).andReturn().getResponse();
            MockHttpServletResponse response2 = mockMvc.perform(
                    put("/api/user/" + user.getUsername())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonUser.write(user).getJson())
            ).andReturn().getResponse();
            //then
            assert response.getStatus() == 403;
            assert response2.getStatus() == 403;
            assert Objects.requireNonNull(userRepository.getUserByUsername("testAdmin").join().orElse(null)).equals(user);
        }

        @Test
        public void shouldReturnForbiddenWhenTryingToDeleteOtherUser() throws Exception {
            //given
            User user = userRepository.getUserByUsername("testAdmin").join().orElse(null);
            assert user != null;
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    delete("/api/user/" + user.getId())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonUser.write(user).getJson())
            ).andReturn().getResponse();
            MockHttpServletResponse response2 = mockMvc.perform(
                    delete("/api/user/" + user.getUsername())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonUser.write(user).getJson())
            ).andReturn().getResponse();
            //then
            assert response.getStatus() == 403;
            assert response2.getStatus() == 403;
            assert Objects.requireNonNull(userRepository.getUserByUsername("testAdmin").join().orElse(null)).equals(user);
        }

        @Nested
        public class SelfModifyingEndpoints {
            @Nested
            public class UpdateEndpoints {
                ObjectMapper mapper = JsonMapper.builder()
                        .configure(MapperFeature.USE_ANNOTATIONS, false)
                        .build();
                @Test
                public void shouldReturnOKIfBodyValid() throws Exception {
                    //no data is changed in this scenario, although the update technically happens anyway
                    //given
                    User user = mockRegister();
                    String token = login(user.getUsername(), user.getPassword());
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    String password = user.getPassword();
                    user.setPassword(null);

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user))
                    ).andReturn().getResponse();
                    //then
                    assert response.getStatus() == 200;
                    user.setPassword(password);
                    assert Objects.requireNonNull(userRepository.getUserByUsername(user.getUsername()).join().orElse(null)).equals(user);
                    //cleanup
                    userRepository.delete(userRepository.getUserByUsername(user.getUsername()).get().orElseThrow());
                }

                @Test
                public void shouldIgnoreChangingId() throws Exception {
                    //given
                    User user = mockRegister();
                    String token = login(user.getUsername(), user.getPassword());
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    long id = user.getId();
                    user.setId(999999999L);
                    String password = user.getPassword();
                    user.setPassword(null);

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user))
                    ).andReturn().getResponse();
                    User updatedUser = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    //then
                    assert response.getStatus() == 200;
                    assert updatedUser.getId().equals(id);
                    user.setId(id);
                    user.setPassword(password);
                    assert Objects.requireNonNull(updatedUser).equals(user);
                    //cleanup
                    userRepository.delete(userRepository.getUserByUsername(user.getUsername()).get().orElseThrow());
                }

                @Test
                public void shouldReturnBadRequestIfUsernameInvalid() throws Exception {
                    //given
                    User user = mockRegister();
                    String token = login(user.getUsername(), user.getPassword());
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    user.setUsername("a");
                    user.setPassword(null);

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user))
                    ).andReturn().getResponse();
                    //then
                    assert response.getStatus() == 400;
                    assert response.getContentAsString().contains("Error: Username must be between 3 and 32 characters long.");
                    //cleanup
                    userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                }
                @Test
                public void shouldReturnBadRequestIfPasswordInvalid() throws Exception {
                    //given
                    User user = mockRegister();
                    String token = login(user.getUsername(), user.getPassword());
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    String password = user.getPassword();
                    user.setPassword("as");

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user))
                    ).andReturn().getResponse();
                    User updatedUser = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    //then
                    assert response.getStatus() == 400;
                    assert response.getContentAsString().contains("Error: Password must be between 6 and 100 characters long.");
                    assert Objects.requireNonNull(updatedUser).getPassword().equals(password);
                    //cleanup
                    userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                }
                @Test
                public void shouldReturnBadRequestIfEmailInvalid() throws Exception {
                    //given
                    User user = mockRegister();
                    String token = login(user.getUsername(), user.getPassword());
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    user.setPassword(null);
                    user.setEmail("asasdasdasdasd");

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user))
                    ).andReturn().getResponse();
                    //then
                    assert response.getStatus() == 400;
                    assert response.getContentAsString().contains("Error: Email must be valid.");
                    //cleanup
                    userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                }

                @Test
                public void shouldIgnoreChangesInRoles() throws Exception {
                    //given
                    User user = mockRegister();
                    String token = login(user.getUsername(), user.getPassword());
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    user.getRoles().add(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().orElseThrow());
                    user.setPassword(null);

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user))
                    ).andReturn().getResponse();
                    User updatedUser = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    //then
                    assert response.getStatus() == 200;
                    assert updatedUser.getRoles().size() == 1;
                    assert updatedUser.getRoles().contains(roleRepository.getRoleByName(ERole.ROLE_USER).get().orElseThrow());
                    //cleanup
                    userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                }
                @Test
                public void shouldReturnConflictIfUsernameAlreadyExists() throws Exception {
                    //given
                    User user = mockRegister();
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    User user2 = mockRegister();
                    String token = login(user2.getUsername(), user2.getPassword());
                    user2.setUsername(user.getUsername());
                    user2.setPassword(null);

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user2))
                    ).andReturn().getResponse();
                    //then
                    assert response.getStatus() == 409;
                    assert response.getContentAsString().contains("Error: Username already exists");
                    //cleanup
                    userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                    userRepository.delete(userRepository.getUserById(user2.getId()).get().orElseThrow());
                }

                @Test
                public void shouldReturnConflictIfEmailAlreadyExists() throws Exception {
                    //given
                    User user = mockRegister();
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    User user2 = mockRegister();
                    user2.setEmail(user.getEmail());
                    String token = login(user2.getUsername(), user2.getPassword());
                    user2.setPassword(null);

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user2))
                    ).andReturn().getResponse();
                    //then
                    assert response.getStatus() == 409;
                    assert response.getContentAsString().contains("Error: Email already exists");
                    //cleanup
                    userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                    userRepository.delete(userRepository.getUserById(user2.getId()).get().orElseThrow());
                }

                @Test
                public void shouldReturnNewEmailIfEmailChanged() throws Exception {
                    //given
                    User user = mockRegister();
                    String token = login(user.getUsername(), user.getPassword());
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    String newEmail = generateRandomString(10) + "@" + generateRandomString(5) + "." + generateRandomString(3);
                    user.setEmail(newEmail);
                    user.setPassword(null);

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user))
                    ).andReturn().getResponse();
                    User updatedUser = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    //then
                    assert response.getStatus() == 200;
                    assert updatedUser.getEmail().equals(newEmail);
                    //cleanup
                    userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                }

                @Test
                public void shouldReturnNewPasswordIfPasswordChanged() throws Exception {
                    //given
                    User user = mockRegister();
                    String token = login(user.getUsername(), user.getPassword());
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    String newPassword = generateRandomString(10);
                    user.setPassword(newPassword);

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user))
                    ).andReturn().getResponse();
                    //then
                    assert response.getStatus() == 200;
                    assert !login(user.getUsername(), newPassword).isEmpty();
                    //cleanup
                    userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                }

                @Test
                public void shouldReturnNewUsernameIfUsernameChanged() throws Exception {
                    //given
                    User user = mockRegister();
                    String token = login(user.getUsername(), user.getPassword());
                    user = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    String newUsername = generateRandomString(10);
                    user.setUsername(newUsername);
                    user.setPassword(null);

                    //when
                    MockHttpServletResponse response = mockMvc.perform(
                            put("/api/user/me")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsString(user))
                    ).andReturn().getResponse();
                    User updatedUser = userRepository.getUserByUsername(user.getUsername()).join().orElseThrow();
                    //then
                    assert response.getStatus() == 200;
                    assert updatedUser.getUsername().equals(newUsername);
                    //cleanup
                    userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                }

            }

            @Test
            public void shouldReturnOKOnDeleteEndpoint() throws Exception {
                //given
                User user = mockRegister();
                String token = login(user.getUsername(), user.getPassword());
                //when
                MockHttpServletResponse response = mockMvc.perform(
                        delete("/api/user/me")
                                .header("Authorization", "Bearer " + token)
                ).andReturn().getResponse();
                //then
                assert response.getStatus() == 200;
                assert userRepository.getUserByUsername(user.getUsername()).join().isEmpty();
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class AdminUserTests {
        private String token = "";
        @BeforeAll
        public void setup() {
            token = login("testAdmin", "admin");
        }
        @Test
        public void shouldReturnOKForGetAllUsers() throws Exception {
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/user/")
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            //then
            assert response.getStatus() == 200;
        }
        @Test
        public void shouldReturnOKForGetUserById() throws Exception {
            //given
            User user = userRepository.findAll().get(0);
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/user/" + user.getId())
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            MockHttpServletResponse response2 = mockMvc.perform(
                    get("/api/user/" + user.getUsername())
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            User parsedUser = jsonUser.parse(response.getContentAsString()).getObject();
            //then
            assert response.getStatus() == 200;
            assert response2.getStatus() == 200;
            assert parsedUser.getUsername().equals(user.getUsername());
            assert parsedUser.getEmail().equals(user.getEmail());
        }
        @Test
        public void shouldReturnEmailForAnyUser() throws Exception {
            //given
            int i = 0;
            User user = userRepository.findAll().get(i);
            while(user.getUsername().equals("testAdmin")) {
                user = userRepository.findAll().get(++i);
            }
            //when
            MockHttpServletResponse response = mockMvc.perform(
                    get("/api/user/" + user.getId())
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            MockHttpServletResponse response2 = mockMvc.perform(
                    get("/api/user/" + user.getUsername())
                            .header("Authorization", "Bearer " + token)
            ).andReturn().getResponse();
            User parsedUser = jsonUser.parse(response.getContentAsString()).getObject();
            User parsedUser2 = jsonUser.parse(response2.getContentAsString()).getObject();
            //then
            assert response.getStatus() == 200;
            assert response2.getStatus() == 200;
            assert parsedUser.getEmail() != null;
            assert parsedUser2.getEmail() != null;
        }
        @Nested
        public class MeEndpoints {
            @Test
            public void shouldReturnOKForGetEndpoint() throws Exception {
                //when
                MockHttpServletResponse response = mockMvc.perform(
                        get("/api/user/me")
                                .header("Authorization", "Bearer " + token)
                ).andReturn().getResponse();
                User parsedUser = jsonUser.parse(response.getContentAsString()).getObject();
                //then
                assert response.getStatus() == 200;
                assert parsedUser.getUsername().equals("testAdmin");
                assert parsedUser.getRoles().size() == 2;
                assert parsedUser.getRoles().contains(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().orElseThrow());
            }
            @Test
            public void shouldReturnOKForPutEndpointWithValidData() throws Exception {
                User user = mockRegister();
                user.setRoles(new HashSet<>());
                user.getRoles().add(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().orElseThrow());
                MockHttpServletResponse response = mockMvc.perform(
                        put("/api/user/" + user.getId())
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                assert response.getStatus() == 200;
                assert userRepository.getUserByUsername(user.getUsername()).join().orElseThrow().getRoles().contains(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().orElseThrow());
                String token = login(user.getUsername(), user.getPassword());
                user.setUsername(generateRandomString(12));
                MockHttpServletResponse response2 = mockMvc.perform(
                        put("/api/user/me")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                User updatedUser = userRepository.getUserById(user.getId()).join().orElseThrow();
                assert response2.getStatus() == 200;
                assert updatedUser.getUsername().equals(user.getUsername());
                assert updatedUser.getRoles().contains(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().orElseThrow());
                //cleanup
                userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
            }

            @Test
            public void shouldReturnOKForDeleteEndpoint() throws Exception {
                User user = mockRegister();
                user.setRoles(new HashSet<>());
                user.getRoles().add(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().orElseThrow());
                MockHttpServletResponse response = mockMvc.perform(
                        put("/api/user/" + user.getId())
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                assert response.getStatus() == 200;
                assert userRepository.getUserByUsername(user.getUsername()).join().orElseThrow().getRoles().contains(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().orElseThrow());
                String token = login(user.getUsername(), user.getPassword());
                MockHttpServletResponse response2 = mockMvc.perform(
                        delete("/api/user/me")
                                .header("Authorization", "Bearer " + token)
                ).andReturn().getResponse();
                assert response.getStatus() == 200;
                assert response2.getStatus() == 200;
                assert userRepository.getUserByUsername(user.getUsername()).join().isEmpty();
            }
        }

        @Nested
        public class IdEndpoints {
            @Test
            public void canDeleteAnyUser() throws Exception {
                //given
                User user = mockRegister();
                User user2 = mockRegister();
                user2.setRoles(new HashSet<>());
                user2.getRoles().add(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().orElseThrow());
                MockHttpServletResponse response = mockMvc.perform(
                        put("/api/user/" + user2.getId())
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user2).getJson())
                ).andReturn().getResponse();
                //when
                MockHttpServletResponse response2 = mockMvc.perform(
                        delete("/api/user/" + user.getUsername())
                                .header("Authorization", "Bearer " + token)
                ).andReturn().getResponse();
                MockHttpServletResponse response3 = mockMvc.perform(
                        delete("/api/user/" + user2.getId())
                                .header("Authorization", "Bearer " + token)
                ).andReturn().getResponse();
                //then
                assert response.getStatus() == 200;
                assert response2.getStatus() == 200;
                assert response3.getStatus() == 200;
                assert userRepository.getUserByUsername(user.getUsername()).join().isEmpty();
                assert userRepository.getUserByUsername(user2.getUsername()).join().isEmpty();
            }

            @Test
            public void canUpdateAnyUser() throws Exception {
                //given
                User user = mockRegister();
                User user2 = mockRegister();
                user2.setRoles(new HashSet<>());
                user2.getRoles().add(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().orElseThrow());
                MockHttpServletResponse response = mockMvc.perform(
                        put("/api/user/" + user2.getId())
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user2).getJson())
                ).andReturn().getResponse();

                //when
                user.setUsername(generateRandomString(12));
                MockHttpServletResponse response2 = mockMvc.perform(
                        put("/api/user/" + user.getId())
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user).getJson())
                ).andReturn().getResponse();
                user2.setEmail(generateRandomString(10) + "@" + generateRandomString(5) + "." + generateRandomString(3));
                user2.setPassword(generateRandomString(12));
                MockHttpServletResponse response3 = mockMvc.perform(
                        put("/api/user/" + user2.getUsername())
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonUser.write(user2).getJson())
                ).andReturn().getResponse();
                //then
                assert response.getStatus() == 200;
                assert response2.getStatus() == 200;
                assert response3.getStatus() == 200;
                assert userRepository.getUserByUsername(user.getUsername()).join().orElseThrow().getUsername().equals(user.getUsername());
                assert userRepository.getUserByUsername(user2.getUsername()).join().orElseThrow().getEmail().equals(user2.getEmail());
                //cleanup
                userRepository.delete(userRepository.getUserById(user.getId()).get().orElseThrow());
                userRepository.delete(userRepository.getUserById(user2.getId()).get().orElseThrow());
            }
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
            System.err.println(e.getMessage());
        }
        return token;
    }

    private User mockRegister() throws Exception {
        User user = new User();
        ObjectMapper mapper = JsonMapper.builder()
                .configure(MapperFeature.USE_ANNOTATIONS, false)
                .build();
        user.setUsername(generateRandomString(10));
        while(userRepository.getUserByUsername(user.getUsername()).get().isPresent()) {
            user.setUsername(generateRandomString(10));
        }
        user.setEmail(generateRandomString(10) + "@test.com");
        while(userRepository.getUserByEmail(user.getEmail()).get().isPresent()) {
            user.setEmail(generateRandomString(10) + "@test.com");
        }
        String password = generateRandomString(10);
        user.setPassword(password);
        user.setRoles(new HashSet<>());
        user.getRoles().add(roleRepository.getRoleByName(ERole.ROLE_USER).get().orElseThrow());
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
        ).andReturn().getResponse();
        assert userRepository.getUserByUsername(user.getUsername()).get().isPresent();
        assert response.getStatus() == 201;
        user = userRepository.getUserByUsername(user.getUsername()).get().orElseThrow();
        user.setPassword(password);
        return user;
    }
}
