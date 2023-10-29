package pl.pollub.f1data.Controllers;


import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Exceptions.EmailExistsException;
import pl.pollub.f1data.Exceptions.UsernameExistsException;
import pl.pollub.f1data.Models.JsonViews.Views;
import pl.pollub.f1data.Models.MessageResponse;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Services.UserService;
import pl.pollub.f1data.Services.impl.UserDetailsImpl;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * @return HTTP 200 with list of users if there are any, HTTP 404 if there are no users (which should never happen), HTTP 500 if there was an error (which also should never happen unless the DB is borked)
     * @apiNote This endpoint is only accessible by users with ADMIN role. It returns a list of all users.
     */
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.getUsers().join();
        if(users == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error: Could not get users."));
        if(users.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: No users found."));
        return ResponseEntity.ok(users);
    }


    /**
     * @param id - can be either id or username
     * @param requestingUser - user that is requesting the data, added by Spring Security
     * @return HTTP 200 with user data if user is found, otherwise error message with HTTP 404
     * @apiNote This endpoint is public, so it can be accessed without logging in. It returns data of a user with given id or username. Email is only returned if user is requesting his own data or an admin is requesting the data.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserByIdOrUsername(@PathVariable String id, @AuthenticationPrincipal UserDetailsImpl requestingUser) {
        //this can be null if user is not logged in - which we permit on this endpoint
        Long requestingUserId = requestingUser != null ? requestingUser.getId() : null;
        User user = userService.getUserByIdOrUsername(id, requestingUserId).join().orElse(null);
        if(user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No user found with id or username " + id + "."));
        return ResponseEntity.ok(user);
    }

    /**
     * @param requestingUser - user that is requesting the data, added by Spring Security
     * @return HTTP 200 with user data if user is found, otherwise error message with HTTP 404 if no user found, or HTTP 401 if user is not logged in
     * @apiNote This endpoint returns data of the user that is currently logged in.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetailsImpl requestingUser) {
        Long userId = requestingUser != null ? requestingUser.getId() : null;
        if(userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: User not logged in."));
        User user = userService.getUserByIdOrUsername(userId.toString()).join().orElse(null);
        if(user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: No user found with id or username " + userId + "."));
        return ResponseEntity.ok(user);
    }

    /**
     * @param id - can be either id or username
     * @param newUser - new user data
     * @return HTTP 200 with user data if user is found and update was successful, otherwise error message with HTTP 400 or 404 if user was not found
     * @apiNote This endpoint is only accessible by users with ADMIN role. It updates data of a user with given id or username with data provided in request body.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User newUser) {
        User userToUpdate = userService.getUserByIdOrUsername(id).join().orElse(null);
        if(userToUpdate == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: No user found with id or username " + id + "."));
        if(newUser.getUsername() != null) {
            if(newUser.getUsername().length() < 3 || newUser.getUsername().length() > 20)
                throw new IllegalArgumentException("Username must be between 3 and 20 characters long.");
            if(!newUser.getUsername().matches("^[a-zA-Z0-9-_]*$"))
                throw new IllegalArgumentException("Username can only contain letters, numbers, dashes and underscores.");
            if(newUser.getUsername().isBlank())
                throw new IllegalArgumentException("Username cannot be blank.");
            UserDetailsImpl usernameExistsCheck = (UserDetailsImpl) userService.getUserByUsername(newUser.getUsername()).join().orElse(null);
            if(usernameExistsCheck != null && !usernameExistsCheck.getId().equals(userToUpdate.getId()))
                throw new UsernameExistsException();
            userToUpdate.setUsername(newUser.getUsername());
        }
        if(newUser.getEmail() != null) {
            if(newUser.getEmail().length() > 100)
                throw new IllegalArgumentException("Email must be less than 100 characters long.");
            if(newUser.getEmail().isBlank())
                throw new IllegalArgumentException("Email cannot be blank.");
            for (User u : userService.getUsers().join()) {
                if(u.getEmail().equals(newUser.getEmail()) && !u.getId().equals(userToUpdate.getId()))
                    throw new EmailExistsException();
            }
            userToUpdate.setEmail(newUser.getEmail());
        }
        if(newUser.getPassword() != null) {
            if(newUser.getPassword().length() < 6 || newUser.getPassword().length() > 100)
                throw new IllegalArgumentException("Password must be between 6 and 100 characters long.");
            if(newUser.getPassword().isBlank())
                throw new IllegalArgumentException("Password cannot be blank.");
            userToUpdate.setPassword(encoder.encode(newUser.getPassword()));
        }
        if(newUser.getRoles() != null)
            userToUpdate.setRoles(newUser.getRoles());
        User result = userService.updateUser(userToUpdate).join().orElse(null);
        if(result == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Could not update user. Check if data is valid."));
        return ResponseEntity.ok(new MessageResponse("User updated successfully."));
    }

    /**
     * @param newUser - new user data
     * @param requestingUser - user that is requesting the data, added by Spring Security
     * @return HTTP 200 with user data if user is found and update was successful, otherwise HTTP 401 if user is not logged in, 404 if user was not found (which should never happen), or 400 if update was unsuccessful
     * @apiNote This endpoint updates data of the currently signed-in user.
     */
    @PutMapping("/me")
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> updateMe(@RequestBody User newUser, @AuthenticationPrincipal UserDetailsImpl requestingUser) {
        Long userId = requestingUser != null ? requestingUser.getId() : null;
        if(userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: User not logged in."));
        //to make sure that this endpoint cannot be abused to change another user's data or give yourself admin role
        newUser.setId(userId);
        newUser.setRoles(null);
        return updateUser(userId.toString(), newUser);
    }

    /**
     * @param id - can be either id or username
     * @return HTTP 200 with message if user is found and delete was successful, otherwise HTTP 404 error if user was not found
     * @apiNote This endpoint is only accessible by users with ADMIN role. It deletes a user (anyone, including themselves) with given id or username.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        User userToDelete = userService.getUserByIdOrUsername(id).join().orElse(null);
        if(userToDelete == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: No user found with id or username " + id + "."));
        return userService.deleteUser(userToDelete.getId()).join();
    }

    /**
     * @param requestingUser - user that is requesting the data, added by Spring Security
     * @return HTTP 200 with message if user is found and delete was successful, otherwise HTTP 401 if user is not logged in
     * @apiNote This endpoint deletes the currently signed-in user, even admins.
     */
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(@AuthenticationPrincipal UserDetailsImpl requestingUser) {
        Long userId = requestingUser != null ? requestingUser.getId() : null;
        if(userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: User not logged in."));
        return deleteUser(userId.toString());
    }

}
