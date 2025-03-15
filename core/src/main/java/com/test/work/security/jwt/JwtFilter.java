package com.test.work.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.work.security.config.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.test.work.security.util.SecurityContextUtil.setSecurityContextHolder;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final SecurityProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        // Получение пути запроса
        var requestPath = request.getRequestURI();

        // Создание AntPathMatcher
        var pathMatcher = new AntPathMatcher();
        if (properties.getExcludedPaths().stream().anyMatch(path -> pathMatcher.match(path, requestPath))) {
            try {
                setSecurityContextHolder(null, request, response);
                filterChain.doFilter(request, response);
            }catch (Exception e){
                handleException(response, e);
            }
            return;
        }

        setSecurityContextHolder(request, response);

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleException(response, e);
        }
    }


    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Error processing request", e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final var mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), "Internal Server Error");
    }

}