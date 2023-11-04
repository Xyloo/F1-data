package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Exceptions.EmailExistsException;
import pl.pollub.f1data.Exceptions.UsernameExistsException;
import pl.pollub.f1data.Models.ERole;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Repositories.UserRepository;
import pl.pollub.f1data.Services.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of {@link UserService}
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    @Async
    public CompletableFuture<Optional<UserDetails>> getUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username).join().orElse(null);
        if (user == null) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        return CompletableFuture.completedFuture(Optional.of(UserDetailsImpl.build(user)));
    }
    @Override
    @Async
    public CompletableFuture<Optional<UserDetails>> getUserById(Long id) {
        User user = userRepository.getUserById(id).join().orElse(null);
        if (user == null) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        return CompletableFuture.completedFuture(Optional.of(UserDetailsImpl.build(user)));
    }

    @Override
    @Async
    public CompletableFuture<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }
    @Override
    @Async
    public CompletableFuture<Optional<User>> getUserByIdOrUsername(String queriedId, Long requestUserId) {
        User user = null;
        User requestUser = userRepository.getUserById(requestUserId).join().orElse(null);

        try {
            user = userRepository.getUserById(Long.parseLong(queriedId)).join().orElse(null);
        } catch (NumberFormatException ignored) {}

        if(user == null) {
            user = userRepository.getUserByUsername(queriedId).join().orElse(null);
        }

        if(user == null) {
            return CompletableFuture.completedFuture(Optional.empty());
        }

        //basically - return email only when user is admin or when user is requesting his own data
        if(requestUser == null){
            user.setEmail(null);
            return CompletableFuture.completedFuture(Optional.of(user));
        }
        if (!Objects.equals(user.getId(), requestUser.getId()) && requestUser.getRoles().stream().noneMatch(role -> role.getName().equals(ERole.ROLE_ADMIN))){
            user.setEmail(null);
        }
        return CompletableFuture.completedFuture(Optional.of(user));
    }

    @Override
    @Async
    //this should be used only by admins
    public CompletableFuture<Optional<User>> getUserByIdOrUsername(String queriedId) {
        User user = null;
        try {
            user = userRepository.getUserById(Long.parseLong(queriedId)).join().orElse(null);
        } catch (NumberFormatException ignored) {
        }
        if (user == null) {
            user = userRepository.getUserByUsername(queriedId).join().orElse(null);
        }
        if (user == null) {
                return CompletableFuture.completedFuture(Optional.empty());
        }
        return CompletableFuture.completedFuture(Optional.of(user));
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username).join().orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return UserDetailsImpl.build(user);
    }

    @Override
    @Async
    //we assume new user data is valid
    public CompletableFuture<Optional<User>> updateUser(User user) {
        User userToUpdate = userRepository.getUserById(user.getId()).join().orElse(null);
        if(userToUpdate == null) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        if(user.getUsername() != null) {
            UserDetailsImpl usernameExistsCheck = (UserDetailsImpl) getUserByUsername(user.getUsername()).join().orElse(null);
            if(usernameExistsCheck != null && !usernameExistsCheck.getId().equals(userToUpdate.getId()))
                throw new UsernameExistsException();
            userToUpdate.setUsername(user.getUsername());
        }
        if(user.getEmail() != null) {
            for (User u : getUsers().join()) {
                if(u.getEmail().equals(user.getEmail()) && !u.getId().equals(userToUpdate.getId()))
                    throw new EmailExistsException();
            }
            userToUpdate.setEmail(user.getEmail());
        }
        if(user.getPassword() != null) {
            userToUpdate.setPassword(encoder.encode(user.getPassword()));
        }
        if(user.getRoles() != null) {
            userToUpdate.setRoles(user.getRoles());
        }
        userRepository.save(userToUpdate);
        return CompletableFuture.completedFuture(Optional.of(userToUpdate));
    }

    @Override
    @Async
    public CompletableFuture<ResponseEntity<?>> deleteUser(Long id) {
        User user = userRepository.getUserById(id).join().orElse(null);
        if(user == null) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."));
        }
        userRepository.delete(user);
        return CompletableFuture.completedFuture(ResponseEntity.ok("User deleted successfully."));
    }
}
