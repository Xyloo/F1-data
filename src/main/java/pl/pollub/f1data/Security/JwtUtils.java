package pl.pollub.f1data.Security;

import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.pollub.f1data.Repositories.UserRepository;
import pl.pollub.f1data.Services.impl.UserDetailsImpl;
import io.jsonwebtoken.*;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for JWT
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${f1data.jwtSecret}")
    private String jwtSecret;

    @Value("${f1data.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    private UserRepository userRepository;

    /**
     * This method generates a JWT token.
     * @param authentication authentication object
     * @return JWT token
     */
    public String generateJwtToken(Authentication authentication)
    {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * This method returns username from JWT token.
     * @param token JWT token
     * @return username embedded in JWT token
     */
    public String getUserNameFromJwtToken(String token)
    {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * This method validates a JWT token. It checks if the token is not expired and if the user with the username embedded in the token exists.
     * Checking for user existence is done to prevent a situation when a user is deleted from the database but his token is still valid.
     * @param authToken JWT token
     * @return true if token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken)
    {
        try {
            String username = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody().getSubject();
            return userRepository.getUserByUsername(username).join().orElse(null) != null;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
