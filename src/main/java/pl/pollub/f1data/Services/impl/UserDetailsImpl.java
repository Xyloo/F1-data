package pl.pollub.f1data.Services.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.pollub.f1data.Models.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserDetails} interface required by Spring Security
 * Enables Spring Security to use {@link User} as a user
 * Quite a lot of boilerplate code and a pain to deal with
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * User id
     */
    private Long id;
    /**
     * Username
     */
    private String username;
    /**
     * Email
     */
    private String email;
    /**
     * Password
     * \@JsonIgnore prevents password from being serialized to JSON
     */
    @JsonIgnore
    private String password;
    /**
     * Granted authorities (roles)
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor
     * @param id id
     * @param username username
     * @param email email
     * @param password password
     * @param authorities granted authorities (roles)
     */
    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id; this.username = username; this.email = email; this.password = password; this.authorities = authorities;
    }

    /**
     * Builds a new {@link UserDetailsImpl} from {@link User}
     * @param user {@link User}
     * @return {@link UserDetailsImpl}
     */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Getter for user id
     * @return user id
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter for user email
     * @return user email
     */
    public String getEmail() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled(){
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
