package main.java.com.test.work.security.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Locale;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Locale locale;

    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Locale locale) {
        super(principal, credentials, authorities);
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}