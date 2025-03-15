package main.java.com.test.work.security.model;

import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@ConfigurationProperties(prefix = "users")
@Getter
@Setter
public class UserProperties {
    private final List<User> users = new ArrayList<>();

    private String admin;
    private String nf;
    private String rusinox;
    private String carbo;
    private String idc;

    @PostConstruct
    public void initUsers() {
        Stream.of(admin, nf, rusinox, carbo, idc).forEach(u -> {
            var data = u.split("/");
            var user = new User(data[0], data[1], data[2]);
            users.add(user);
        });
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class User {
        private String login;
        private String password;
        private String role;
    }
}