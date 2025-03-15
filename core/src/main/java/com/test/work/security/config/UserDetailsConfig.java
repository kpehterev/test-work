package main.java.com.test.work.security.config;

import com.test.work.security.model.UserProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UserDetailsConfig {
    private final UserProperties userProperties;
    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> users = userProperties.getUsers().stream()
                .map(user -> User.builder()
                        .username(user.getLogin())
                        .password(passwordEncoder().encode(user.getPassword()))
                        .roles(user.getRole())
                        .build())
                .collect(Collectors.toList());

        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}