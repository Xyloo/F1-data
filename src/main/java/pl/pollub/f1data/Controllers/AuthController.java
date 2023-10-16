package pl.pollub.f1data.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Exceptions.EmailExistsException;
import pl.pollub.f1data.Exceptions.UsernameExistsException;
import pl.pollub.f1data.Models.DTOs.CreateUserDTO;
import pl.pollub.f1data.Models.DTOs.LoginUserDTO;
import pl.pollub.f1data.Models.ERole;
import pl.pollub.f1data.Models.JwtResponse;
import pl.pollub.f1data.Models.MessageResponse;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Repositories.RoleRepository;
import pl.pollub.f1data.Repositories.UserRepository;
import pl.pollub.f1data.Security.JwtUtils;
import pl.pollub.f1data.Services.impl.UserDetailsImpl;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        if(userRepository.getUserByUsername(createUserDTO.getUsername()).join().isPresent()) {
            throw new UsernameExistsException(); //testing
        }
        if(userRepository.getUserByEmail(createUserDTO.getEmail()).join().isPresent()) {
            throw new EmailExistsException(); //testing
        }
        User user = new User(createUserDTO.getUsername(), createUserDTO.getEmail(), encoder.encode(createUserDTO.getPassword()));
        user.setRoles(Set.of(roleRepository.getRoleByName(ERole.ROLE_USER).join().orElseThrow()));
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }
}
