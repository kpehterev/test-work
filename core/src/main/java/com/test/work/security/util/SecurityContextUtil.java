package com.test.work.security.util;

import com.test.work.security.model.CustomAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.Locale;

import static com.test.work.security.jwt.JwtTokenUtil.validateAndExtractRolesFromToken;
import static com.test.work.util.LocaleUtil.determineLocale;


@Slf4j
public class SecurityContextUtil {

    public static void setSecurityContextHolder(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ")) {

            try {
                var roles = validateAndExtractRolesFromToken(request);
                if (!roles.isEmpty()) {
                    setSecurityContextHolder(roles, request, response);
                } else {
                    throw new Exception("empty roles");
                }
            }catch (ExpiredJwtException e){
                log.error("Error expired JWT token", e);
                throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
            } catch (Exception e) {
                log.error("Error processing JWT token", e);
                throw new AuthenticationException(e.getMessage());
            }
        }
    }

    private static List<SimpleGrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private static Authentication createAuthentication(List<SimpleGrantedAuthority> authorities, Locale locale) {
        return new CustomAuthenticationToken(null, null, authorities, locale);
    }

    public static void setSecurityContextHolder(@Nullable List<String> roles, HttpServletRequest request, HttpServletResponse response){
        var locale = determineLocale(request);
        response.setLocale(locale);
        Authentication authentication;
        if (roles != null) {
            var authorities = convertRolesToAuthorities(roles);
            authentication = createAuthentication(authorities, locale);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
            authentication = createAuthentication(null, locale);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
