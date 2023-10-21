package pl.pollub.f1data.Controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

import java.security.SecureRandom;
import java.util.Base64;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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

    //I am not sure if this works at all...
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        if(authentication == null) {
            return ResponseEntity.badRequest().body("User is not logged in!");
        }
        logger.info("logoutUser: " + authentication.getName());
        logoutHandler.setClearAuthentication(true);
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                logger.info("Cookie: "+ cookie.getName());
                Cookie cookieToDelete = new Cookie(cookie.getName(), null);
                cookieToDelete.setMaxAge(0);
                response.addCookie(cookieToDelete);
            }
        }
        logoutHandler.logout(request, response, authentication);
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> resetPassword(Authentication authentication) {
        if(authentication == null) {
            return ResponseEntity.badRequest().body("User is not logged in!");
        }
        logger.info("resetPassword: " + authentication.getName());
        User user = userRepository.getUserByUsername(authentication.getName()).join().orElse(null);
        if(user == null) {
            return ResponseEntity.badRequest().body("User not found!");
        }

        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[16];
        secureRandom.nextBytes(token);
        String newPassword = Base64.getUrlEncoder().withoutPadding().encodeToString(token);
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(new Object() {
            public final String message = "Password reset successfully!";
            public final String password = newPassword;
        });

    }
}
