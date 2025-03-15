package com.test.work.security;

import com.test.work.exception.ErrorResponseException;
import com.test.work.model.UserCredits;
import com.test.work.model.UserTokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.test.work.security.config.SecurityContext.getUserRoleByLogin;
import static com.test.work.security.jwt.JwtTokenUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public UserTokens authenticate(UserCredits userCredits) throws ErrorResponseException {
        try {
            if (nonValidEmail(userCredits.getLogin())) {
                throw new IllegalArgumentException("Invalid email format");
            }

            final var userDetails = userDetailsService.loadUserByUsername(userCredits.getLogin());
            if (passwordEncoder.matches(userCredits.getPassword(), userDetails.getPassword())) {
                UserTokens user = new UserTokens();
                user.setAccessToken(generateAccessToken(getUserRoleByLogin(userCredits.getLogin()).getValue()));
                user.setRefreshToken(generateRefreshToken(getUserRoleByLogin(userCredits.getLogin()).getValue()));
                return user;
            }
            throw new Exception("Invalid credentials");
        } catch (Exception e) {
            log.error("Denied authenticate",e);
            //throw generateAccessException(e.getMessage());
            throw new RuntimeException();
        }
    }

    private boolean nonValidEmail(String login) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(login);
        return !matcher.matches();
    }

    public UserTokens refreshToken(UserTokens userTokens) throws ErrorResponseException {
        try {
            validateAccessToken(userTokens.getAccessToken());
            validateRefreshToken(userTokens.getRefreshToken());
            UserTokens newUserTokens = new UserTokens();
            newUserTokens.setAccessToken(userTokens.getAccessToken());
            newUserTokens.setRefreshToken(generateRefreshToken("ROLE"));
            return newUserTokens;
        }catch (Exception e){
            //throw generateAccessException(e.getMessage());
            throw new RuntimeException();
        }
    }

}