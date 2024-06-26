package pl.pollub.f1data.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Response for JWT authentication
 */
@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    /**
     * Constructor
     * @param token JWT token
     * @param id user id
     * @param username username
     * @param email email
     * @param roles user roles
     */
    public JwtResponse(String token, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
