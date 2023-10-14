package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Repositories.UserRepository;
import pl.pollub.f1data.Services.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<User> CreateUser(User user) {
        User savedUser = userRepository.save(user);  // Use the save method to create the user
        return Optional.ofNullable(savedUser);
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
    public Optional<List<User>> GetUsers() {
        return Optional.empty();
    }

    @Override
    public boolean VerifyPassword(User user, String providedPassword) {
        return false;
    }
}
