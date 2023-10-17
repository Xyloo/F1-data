package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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

    @Override
    public Optional<UserDetails> GetUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username).join().orElse(null);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(UserDetailsImpl.build(user));
    }

    @Override
    @Async
    public CompletableFuture<List<User>> GetUsers() {
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetails> user = GetUserByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user.get();
    }
}
