package pl.pollub.f1data.Controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Models.User;
import pl.pollub.f1data.Services.UserService;
import pl.pollub.f1data.Services.impl.UserDetailsImpl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public CompletableFuture<List<User>> getUsers() {
        return userService.GetUsers();
    }

    @GetMapping("/{id}")
    public CompletableFuture<Optional<User>> getUserByIdOrUsername(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = authentication.getPrincipal() instanceof UserDetailsImpl ? ((UserDetailsImpl) authentication.getPrincipal()).getId() : null;
        return userService.GetUserByIdOrUsername(id, userId);
    }

}
