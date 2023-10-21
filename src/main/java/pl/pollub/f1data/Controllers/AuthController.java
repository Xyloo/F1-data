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

    /**
     * @param loginUserDTO - username and password
     * @return HTTP 200 with JWT token if login is successful, otherwise HTTP 401 with message "Invalid username or password."
     * @apiNote This endpoint is public. It returns a JWT token which users use to authenticate if login is successful.
     */
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

    /**
     * @param createUserDTO - username, email and password. Username and email must be unique.
     * @return HTTP 200 with message "User registered successfully!" if registration is successful, otherwise HTTP 400 with message "Username already exists." or "Email already exists."
     * @apiNote This endpoint is public. It registers a new user or throws an exception if username or email already exists. It does not log the user in.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        if(userRepository.getUserByUsername(createUserDTO.getUsername()).join().isPresent()) {
            throw new UsernameExistsException();
        }
        if(userRepository.getUserByEmail(createUserDTO.getEmail()).join().isPresent()) {
            throw new EmailExistsException();
        }
        User user = new User(createUserDTO.getUsername(), createUserDTO.getEmail(), encoder.encode(createUserDTO.getPassword()));
        user.setRoles(Set.of(roleRepository.getRoleByName(ERole.ROLE_USER).join().orElseThrow()));
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

    /**
     * @param authentication - user that is requesting the data, added by Spring Security
     * @param request - request data
     * @param response - response data
     * @return HTTP 200 with message "User logged out successfully!" if logout is successful, otherwise HTTP 400 with message "User is not logged in!"
     * @apiNote This endpoint requires the user to be logged in. It logs the user out by clearing the authentication and deleting all cookies. It does not invalidate the JWT token - it is still valid until it expires.
     */
    //I am not sure if this works at all...
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        if(authentication == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("User is not logged in!"));
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

}
