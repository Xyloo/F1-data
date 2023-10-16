package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Exceptions.EmailExistsException;
import pl.pollub.f1data.Exceptions.UsernameExistsException;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Repositories.UserRepository;
import pl.pollub.f1data.Services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CompletableFuture<Optional<User>> createUser(User userData) {
        return userRepository.getUserByUsername(userData.getUsername()).thenCombineAsync(
                userRepository.getUserByEmail(userData.getEmail()),
                (userExists, userExistsEmail) -> {
                    if (userExists.isPresent()) {
                        throw new UsernameExistsException();
                    }
                    if (userExistsEmail.isPresent()) {
                        throw new EmailExistsException();
                    }
                    User user = new User();
                    user.setUsername(userData.getUsername());
                    user.setPassword(passwordEncoder.encode(userData.getPassword()));
                    user.setEmail(userData.getEmail());
                    user.setRoles(userData.getRoles());
                    return userRepository.save(user);
                }
        ).thenApply(Optional::ofNullable);
    }

    @Override
    public String GenerateToken(User user) {
        return null;
    }

    @Override
    public Optional<User> GetUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    @Async
    public CompletableFuture<List<User>> GetUsers() {
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    @Override
    public boolean VerifyPassword(User user, String providedPassword) {
        return false;
    }
}
