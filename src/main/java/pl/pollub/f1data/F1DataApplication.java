package pl.pollub.f1data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pollub.f1data.Models.ERole;
import pl.pollub.f1data.Models.Role;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Repositories.RoleRepository;
import pl.pollub.f1data.Repositories.UserRepository;

import java.util.HashSet;

@SpringBootApplication
public class F1DataApplication {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(F1DataApplication.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return args -> {

            if(roleRepository.getRoleByName(ERole.ROLE_USER).get().isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_USER));
            }
            if(roleRepository.getRoleByName(ERole.ROLE_ADMIN).get().isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
            }

            if(userRepository.getUserByUsername("testAdmin").get().isEmpty()) {
                User u = new User("testAdmin", "admin@f1-data.com", encoder.encode("admin"));
                u.setRoles(new HashSet<>(roleRepository.findAll()));
                userRepository.save(u);
            }

            if(userRepository.getUserByUsername("testUser").get().isEmpty()) {
                User u = new User("testUser", "user@f1-data.com", encoder.encode("user"));
                u.setRoles(new HashSet<>());
                u.getRoles().add(roleRepository.getRoleByName(ERole.ROLE_USER).get().get());
                userRepository.save(u);
            }
        };
    }
}
