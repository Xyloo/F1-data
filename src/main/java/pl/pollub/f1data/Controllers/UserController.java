package pl.pollub.f1data.Controllers;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Services.UserService;
import pl.pollub.f1data.Services.impl.UserDetailsImpl;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * @return HTTP 200 with list of users if there are any, otherwise HTTP 200 with message "No users found." which should never happen.
     * @apiNote This endpoint is only accessible by users with ADMIN role. It returns a list of all users.
     */
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.GetUsers().join();
        if(users == null)
            return ResponseEntity.badRequest().body("Could not get users.");
        if(users.isEmpty())
            return ResponseEntity.ok("No users found.");
        return ResponseEntity.ok(users);
    }


    /**
     * @param id - can be either id or username
     * @param requestingUser - user that is requesting the data, added by Spring Security
     * @return HTTP 200 with user data if user is found, otherwise error message with HTTP 400
     * @apiNote This endpoint is public, so it can be accessed without logging in. It returns data of an user with given id or username. Email is only returned if user is requesting his own data or an admin is requesting the data.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserByIdOrUsername(@PathVariable String id, @AuthenticationPrincipal UserDetailsImpl requestingUser) {
        //this can be null if user is not logged in - which we permit on this endpoint
        Long requestingUserId = requestingUser != null ? requestingUser.getId() : null;
        User user = userService.GetUserByIdOrUsername(id, requestingUserId).join().orElse(null);
        if(user == null)
            return ResponseEntity.badRequest().body("No user found with id or username " + id + ".");
        return ResponseEntity.ok(user);
    }

    /**
     * @param requestingUser - user that is requesting the data, added by Spring Security
     * @return HTTP 200 with user data if user is found, otherwise error message with HTTP 400
     * @apiNote This endpoint returns data of the user that is currently logged in.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetailsImpl requestingUser) {
        Long userId = requestingUser != null ? requestingUser.getId() : null;
        if(userId == null)
            return ResponseEntity.badRequest().body("User not logged in.");
        User user = userService.GetUserByIdOrUsername(userId.toString()).join().orElse(null);
        if(user == null)
            return ResponseEntity.badRequest().body("No user found with id or username " + userId + ".");
        return ResponseEntity.ok(user);
    }

    /**
     * @param id - can be either id or username
     * @param newUser - new user data
     * @return HTTP 200 with user data if user is found and update was successful, otherwise error message with exception details with HTTP 400
     * @apiNote This endpoint is only accessible by users with ADMIN role. It updates data of a user with given id or username with data provided in request body.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User newUser) {
        User userToUpdate = userService.GetUserByIdOrUsername(id).join().orElse(null);
        if(userToUpdate == null)
            return ResponseEntity.badRequest().body("No user found with id or username " + id + ".");
        if(newUser.getUsername() != null)
            userToUpdate.setUsername(newUser.getUsername());
        if(newUser.getEmail() != null)
            userToUpdate.setEmail(newUser.getEmail());
        if(newUser.getPassword() != null)
            userToUpdate.setPassword(newUser.getPassword());
        if(newUser.getRoles() != null)
            userToUpdate.setRoles(newUser.getRoles());
        User result = userService.UpdateUser(userToUpdate).join().orElse(null);
        if(result == null)
            return ResponseEntity.badRequest().body("Could not update user. Check if data is valid.");
        return ResponseEntity.ok("User updated successfully.");
    }

    /**
     * @param newUser - new user data
     * @param requestingUser - user that is requesting the data, added by Spring Security
     * @return HTTP 200 with user data if user is found and update was successful, otherwise error message with exception details with HTTP 400
     * @apiNote This endpoint updates data of the currently signed-in user.
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@RequestBody User newUser, @AuthenticationPrincipal UserDetailsImpl requestingUser) {
        Long userId = requestingUser != null ? requestingUser.getId() : null;
        if(userId == null)
            return ResponseEntity.badRequest().body("User not logged in.");
        return updateUser(userId.toString(), newUser);
    }

}
