package UnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import pl.pollub.f1data.Models.ERole;
import pl.pollub.f1data.Models.Role;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Repositories.UserRepository;
import pl.pollub.f1data.Services.impl.UserServiceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("user@user.com");
        user.setRoles(new HashSet<Role>() {{
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


}
