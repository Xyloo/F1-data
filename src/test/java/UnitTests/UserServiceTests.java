package UnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import pl.pollub.f1data.Exceptions.EmailExistsException;
import pl.pollub.f1data.Exceptions.UsernameExistsException;
import pl.pollub.f1data.Models.ERole;
import pl.pollub.f1data.Models.Role;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Repositories.UserRepository;
import pl.pollub.f1data.Services.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("user@user.com");
        user.setRoles(new HashSet<>() {{
            add(new Role(ERole.ROLE_USER));
        }});
    }

    @Test
    public void givenExistingUsername_whenGetUserByUsername_thenUserShouldBeFound() {
        //given
        given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
        //when
        Optional<UserDetails> returnedUser = userService.getUserByUsername(user.getUsername()).join();
        //then
        assert returnedUser.isPresent();
        assert returnedUser.get().getUsername().equals(user.getUsername());
    }

    @Test
    public void givenNonExistingUsername_whenGetUserByUsername_thenUserShouldNotBeFound() {
        //given
        given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        //when
        Optional<UserDetails> returnedUser = userService.getUserByUsername(user.getUsername()).join();
        //then
        assert returnedUser.isEmpty();
    }

    @Test
    public void givenExistingId_whenGetUserById_thenUserShouldBeFound() {
        //given
        given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
        //when
        Optional<UserDetails> returnedUser = userService.getUserById(user.getId()).join();
        //then
        assert returnedUser.isPresent();
        assert returnedUser.get().getUsername().equals(user.getUsername());
    }

    @Test
    public void givenNonExistingId_whenGetUserById_thenUserShouldNotBeFound() {
        //given
        given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        //when
        Optional<UserDetails> returnedUser = userService.getUserById(user.getId()).join();
        //then
        assert returnedUser.isEmpty();
    }

    @Test
    public void givenAListOfUsers_whenGetUsers_thenUsersShouldBeFound() {
        //given
        given(userRepository.findAll()).willReturn(new ArrayList<>() {{
            add(user);
        }});
        //when
        List<User> returnedUsers = userService.getUsers().join();
        //then
        assert returnedUsers.size() == 1;
        assert returnedUsers.contains(user);
    }

    @Test
    public void givenExistingUsername_whenGetUserByIdOrUsername_thenUserShouldBeFound() {
        //given
        given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
        given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        //when
        Optional<User> returnedUser = userService.getUserByIdOrUsername(user.getUsername(), user.getId()).join();
        //then
        assert returnedUser.isPresent();
        assert returnedUser.get().getUsername().equals(user.getUsername());
    }

    @Test
    public void givenNonExistingUsername_whenGetUserByIdOrUsername_thenUserShouldNotBeFound() {
        //given
        given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        //when
        Optional<User> returnedUser = userService.getUserByIdOrUsername(user.getUsername(), user.getId()).join();
        //then
        assert returnedUser.isEmpty();
    }

    @Test
    public void givenExistingId_whenGetUserByIdOrUsername_thenUserShouldBeFound() {
        //given
        given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
        //when
        Optional<User> returnedUser = userService.getUserByIdOrUsername(user.getId().toString(), user.getId()).join();
        //then
        assert returnedUser.isPresent();
        assert returnedUser.get().getUsername().equals(user.getUsername());
    }

    @Test
    public void givenNonExistingId_whenGetUserByIdOrUsername_thenUserShouldNotBeFound() {
        //given
        given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        given(userRepository.getUserByUsername(user.getId().toString())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        //when
        Optional<User> returnedUser = userService.getUserByIdOrUsername(user.getId().toString(), user.getId()).join();
        //then
        assert returnedUser.isEmpty();
    }

    @Test
    public void givenExistingUsername_whenGetUserByIdOrUsernameAsAdmin_thenUserShouldBeFound() {
        //given
        given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
        //when
        Optional<User> returnedUser = userService.getUserByIdOrUsername(user.getUsername()).join();
        //then
        assert returnedUser.isPresent();
        assert returnedUser.get().getUsername().equals(user.getUsername());
    }

    @Test
    public void givenNonExistingUsername_whenGetUserByIdOrUsernameAsAdmin_thenUserShouldNotBeFound() {
        //given
        given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        //when
        Optional<User> returnedUser = userService.getUserByIdOrUsername(user.getUsername()).join();
        //then
        assert returnedUser.isEmpty();
    }

    @Test
    public void givenExistingId_whenGetUserByIdOrUsernameAsAdmin_thenUserShouldBeFound() {
        //given
        given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
        //when
        Optional<User> returnedUser = userService.getUserByIdOrUsername(user.getId().toString()).join();
        //then
        assert returnedUser.isPresent();
        assert returnedUser.get().getUsername().equals(user.getUsername());
    }

    @Test
    public void givenNonExistingId_whenGetUserByIdOrUsernameAsAdmin_thenUserShouldNotBeFound() {
        //given
        given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        given(userRepository.getUserByUsername(user.getId().toString())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
        //when
        Optional<User> returnedUser = userService.getUserByIdOrUsername(user.getId().toString()).join();
        //then
        assert returnedUser.isEmpty();
    }

    @Nested
    public class UpdateUserTests {
        @Test
        public void givenExistingUser_whenUpdateUser_thenUserShouldBeUpdated() {
            //given
            given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.save(user)).willReturn(user);
            user.setPassword(null);
            //when
            Optional<User> returnedUser = userService.updateUser(user).join();
            //then
            assert returnedUser.isPresent();
            assert returnedUser.get().getUsername().equals(user.getUsername());
            assert returnedUser.get().getEmail().equals(user.getEmail());
            assert returnedUser.get().getRoles().equals(user.getRoles());
        }

        @Test
        public void givenNonExistingUser_whenUpdateUser_thenUserShouldNotBeUpdated() {
            //given
            given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.empty()));
            //when
            Optional<User> returnedUser = userService.updateUser(user).join();
            //then
            assert returnedUser.isEmpty();
        }

        @Test
        public void givenExistingUserWithNewValidUsername_whenUpdateUser_thenUserShouldBeUpdated() {
            //given
            given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.getUserByUsername("newUsername")).willReturn(CompletableFuture.completedFuture(Optional.empty()));
            given(userRepository.save(user)).willReturn(user);
            user.setUsername("newUsername");
            user.setPassword(null);
            //when
            Optional<User> returnedUser = userService.updateUser(user).join();
            //then
            assert returnedUser.isPresent();
            assert returnedUser.get().getUsername().equals("newUsername");
            assert returnedUser.get().getEmail().equals(user.getEmail());
            assert returnedUser.get().getRoles().equals(user.getRoles());
        }

        @Test
        public void givenExistingUserWithNewValidEmail_whenUpdateUser_thenUserShouldBeUpdated() {
            //given
            given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.save(user)).willReturn(user);
            user.setPassword(null);
            user.setEmail("user@testing.com");
            //when
            Optional<User> returnedUser = userService.updateUser(user).join();
            //then
            assert returnedUser.isPresent();
            assert returnedUser.get().getUsername().equals(user.getUsername());
            assert returnedUser.get().getEmail().equals("user@testing.com");
            assert returnedUser.get().getRoles().equals(user.getRoles());
        }

        @Test
        public void givenExistingUserWithNewPassword_whenUpdateUser_thenUserShouldBeUpdated() {
            //given
            given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.save(user)).willReturn(user);
            user.setPassword("newPassword");
            //when
            Optional<User> returnedUser = userService.updateUser(user).join();
            //then
            assert returnedUser.isPresent();
            assert returnedUser.get().getUsername().equals(user.getUsername());
            assert returnedUser.get().getEmail().equals(user.getEmail());
            assert returnedUser.get().getRoles().equals(user.getRoles());
            assert encoder.matches("newPassword", returnedUser.get().getPassword());
        }
        @Test
        public void givenExistingUserWithNewInvalidUsername_whenUpdateUser_thenUserShouldNotBeUpdated() {
            User user2 = new User();
            user2.setId(2L);
            user2.setUsername("user2");
            user2.setPassword("password");
            user2.setEmail("user2@test.com");
            user2.setRoles(new HashSet<>() {{
                add(new Role(ERole.ROLE_USER));
            }});
            //given
            given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.getUserByUsername("newUsername")).willReturn(CompletableFuture.completedFuture(Optional.of(user2)));
            //when
            user.setUsername("newUsername");
            Exception exception = assertThrows(UsernameExistsException.class, () -> userService.updateUser(user).join());
            //then
            assert exception.getMessage().contains("Username already exists");

        }
        @Test
        public void givenExistingUserWithNewInvalidEmail_whenUpdateUser_thenUserShouldNotBeUpdated() {
            User user2 = new User();
            user2.setId(2L);
            user2.setUsername("user2");
            user2.setPassword("password");
            user2.setEmail("user2@test.com");
            user2.setRoles(new HashSet<>() {{
                add(new Role(ERole.ROLE_USER));
            }});
            //given
            given(userRepository.getUserById(user.getId())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.getUserByUsername(user.getUsername())).willReturn(CompletableFuture.completedFuture(Optional.of(user)));
            given(userRepository.findAll()).willReturn(new ArrayList<>() {{
                add(user);
                add(user2);
            }});
            //when
            user.setEmail("user2@test.com");
            Exception exception = assertThrows(EmailExistsException.class, () -> userService.updateUser(user).join());
            //then
            assert exception.getMessage().contains("Email already exists");
        }
    }
}
