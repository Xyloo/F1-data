package pl.pollub.f1data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.pollub.f1data.Models.ERole;
import pl.pollub.f1data.Models.Role;
import pl.pollub.f1data.Repositories.RoleRepository;

@SpringBootApplication
public class F1DataApplication {
    @Autowired
    private RoleRepository roleRepository;

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
        };
    }
}
