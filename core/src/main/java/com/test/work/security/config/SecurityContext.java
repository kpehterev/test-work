package com.test.work.security.config;

import com.test.work.security.model.CustomAuthenticationToken;
import com.test.work.security.model.Role;
import com.test.work.security.model.UserContext;
import com.test.work.security.model.UserProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class SecurityContext {

    private final static Map<String, UserContext> userList = new HashMap<>();

    private final UserProperties userProperties;

    @PostConstruct
    private void init(){
        for (UserProperties.User user : userProperties.getUsers()) {
            userList.put(user.getLogin(), UserContext.builder()
                    .name(user.getLogin())
                    .password(user.getPassword())
                    .role(Role.valueOf(user.getRole().toUpperCase()))
                    .build());
        }
    }

    public static Locale getLocale(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CustomAuthenticationToken customAuthentication) {
            return customAuthentication.getLocale();
        }
        return Locale.US;
    }

    public static Role getUserRole(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CustomAuthenticationToken customAuthentication) {
            return customAuthentication.getAuthorities().stream()
                    .findFirst().map(role -> Role.valueOf(role.getAuthority().toUpperCase())).orElseThrow();
        }
        return null;
    }

    public static Role getUserRoleByLogin(String login){
        return userList.get(login).getRole();
    }
}