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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Exceptions.EmailExistsException;
import pl.pollub.f1data.Exceptions.UsernameExistsException;
import pl.pollub.f1data.Models.JsonViews.Views;
import pl.pollub.f1data.Models.MessageResponse;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Services.UserService;
import pl.pollub.f1data.Services.impl.UserDetailsImpl;

import java.util.List;

/**
 * This class is responsible for handling requests related to users, such as getting, updating and deleting users.
 */
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
     * This endpoint returns all users and is only accessible by users with admin role.
     * @return <p>• HTTP 200 with list of users if there are any</p>
     * <p>• HTTP 404 if there are no users (which should never happen)</p>
     * <p>• HTTP 500 if there was an error (which also should never happen unless the DB is broken)</p>
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
     * This endpoint returns data of a user with given id or username and is public.
     * It can be accessed without logging in. Email is only returned if user is requesting his own data or an admin is requesting the data.
     * @param id can be either id or username
     * @param requestingUser user that is requesting the data, added by Spring Security
     * @return <p>• HTTP 200 with user data if user is found</p>
     * <p>• HTTP 404 if user was not found</p>
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
     * This endpoint returns data of the user that is currently logged in.
     * @param requestingUser user that is requesting the data, added by Spring Security
     * @return <p>• HTTP 200 with user data if user is found</p>
     * <p>• HTTP 401 if user is not logged in</p>
     * <p>• HTTP 404 if user was not found (which should never happen)</p>
     * @see UserController#getUserByIdOrUsername(String, UserDetailsImpl)
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
     * This endpoint updates data of a user with given id or username with data provided in request body and is only accessible by users with admin role.
     * @param id can be either id or username
     * @param newUser new user data
     * @return <p>• HTTP 200 with message if user is found and update was successful</p>
     * <p>• HTTP 401 if user is not logged in</p>
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> updateUser(@PathVariable String id, @Validated(Views.ValidateUserInfo.class) @RequestBody User newUser) {
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
     * This endpoint updates data of the currently signed-in user.
     * @param newUser new user data
     * @param requestingUser user that is requesting the data, added by Spring Security
     * @return <p>• HTTP 200 with message if user is found and update was successful</p>
     * <p>• HTTP 401 if user is not logged in</p>
     * <p>• HTTP 404 if user was not found (which should never happen)</p>
     * <p>• HTTP 400 if data is invalid</p>
     * @see UserController#updateUser(String, User)
     */
    @PutMapping("/me")
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> updateMe(@Validated(Views.ValidateUserInfo.class) @RequestBody User newUser, @AuthenticationPrincipal UserDetailsImpl requestingUser) {
        Long userId = requestingUser != null ? requestingUser.getId() : null;
        if(userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: User not logged in."));
        //to make sure that this endpoint cannot be abused to change another user's data or give yourself admin role
        newUser.setId(userId);
        newUser.setRoles(null);
        return updateUser(userId.toString(), newUser);
    }

    /**
     * This endpoint deletes a user <b>(anyone, including themselves)</b> with given id or username and is only accessible by users with admin role.
     * @param id can be either id or username
     * @return <p>• HTTP 200 with message if user is found and delete was successful</p>
     * <p>• HTTP 401 if user is not logged in</p>
     * <p>• HTTP 404 if user was not found</p>
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
     * This endpoint deletes the currently signed-in user, <b>even admins.</b>
     * @param requestingUser user that is requesting the data, added by Spring Security
     * @return <p>• HTTP 200 with message if user is found and delete was successful</p>
     * <p>• HTTP 401 if user is not logged in</p>
     * @see UserController#deleteUser(String)
     */
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(@AuthenticationPrincipal UserDetailsImpl requestingUser) {
        Long userId = requestingUser != null ? requestingUser.getId() : null;
        if(userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: User not logged in."));
        return deleteUser(userId.toString());
    }

}
