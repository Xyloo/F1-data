package pl.pollub.f1data.Models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pollub.f1data.Models.JsonViews.Views;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User entity
 */
@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private Long id;

    @NotBlank(message = "Username cannot be blank.", groups = Views.NewUserInfo.class)
    @Size(min = 3, max = 32, message = "Username must be between 3 and 32 characters long.", groups = Views.ValidateUserInfo.class)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9-_]*$", message = "Username can only contain letters, numbers, dashes and underscores. It also cannot start with a number.", groups = Views.ValidateUserInfo.class)
    @JsonView(Views.Public.class)
    private String username;

    @NotBlank(message = "Email cannot be blank.", groups = Views.NewUserInfo.class)
    @Size(min = 2, max = 100, message = "Email must be less than 100 characters long.", groups = Views.ValidateUserInfo.class)
    @Email(message = "Email must be valid.", groups = Views.ValidateUserInfo.class)
    @JsonView(Views.Public.class)
    private String email;

    @NotBlank(message = "Password cannot be blank.", groups = Views.NewUserInfo.class)
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters long.", groups = Views.ValidateUserInfo.class)
    @JsonView(Views.Internal.class)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false))
    @JsonView(Views.Internal.class)
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructor
     * @param username username
     * @param email email
     * @param password password
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username=" + username +
                ", email=" + email +
                ", password=" + password +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                roles.size() == user.roles.size() &&
                roles.containsAll(user.roles) &&
                user.roles.containsAll(roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, roles);
    }
}