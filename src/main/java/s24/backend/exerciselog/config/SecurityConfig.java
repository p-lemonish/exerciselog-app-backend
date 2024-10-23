package s24.backend.exerciselog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import s24.backend.exerciselog.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/login","/h2-console/**","/register","/access-denied").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
            .loginPage("/login")
            .permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
            .rememberMe(remember -> remember.tokenValiditySeconds(1209600)) // 14 day token for rememberme
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())) //fix for h2-console not working
            .userDetailsService(customUserDetailsService)
            .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}