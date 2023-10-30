package pl.pollub.f1data.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.pollub.f1data.Services.impl.UserServiceImpl;

/**
 * Security and authentication configuration
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig{
    @Autowired
    UserServiceImpl userService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * This method creates a new {@link AuthTokenFilter}
     * This filter is used to authenticate users. It is a part of the authentication process.
     * @return {@link AuthTokenFilter}
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * This method creates a new {@link DaoAuthenticationProvider}
     * It is configured to use {@link UserServiceImpl} as a user details service and {@link BCryptPasswordEncoder} as a password encoder.
     * It is a part of the authentication process.
     * @return {@link DaoAuthenticationProvider}
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * This method creates a new {@link AuthenticationManager}
     * It is a part of the authentication process.
     * @param authConfig {@link AuthenticationConfiguration}
     * @return {@link AuthenticationManager}
     * @throws Exception if authentication manager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * This method creates a new {@link BCryptPasswordEncoder}
     * It is essential for security - it hashes passwords.
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
//        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
//        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
//        delegate.setCsrfRequestAttributeName("_csrf");
//        CsrfTokenRequestHandler requestHandler = delegate;

        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers().permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                        .csrf(csrf -> csrf.disable());
                /*
                .csrf(csrf-> csrf.ignoringRequestMatchers(request -> request
                        .getRequestURI().startsWith("/api/auth/sign") && request.getMethod().equals("POST"))
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                )
                it really doesn't want to work */
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
