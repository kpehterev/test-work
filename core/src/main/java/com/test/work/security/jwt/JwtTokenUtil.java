package main.java.com.test.work.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class JwtTokenUtil {

    private static final String RSA_PRIVATE_KEY_PATH = "/etc/secrets/dev/privatekey.pem";
    private static final String RSA_PUBLIC_KEY_PATH = "/etc/secrets/dev/publickey.pem";
    private static final String HS256_SECRET_KEY_PATH = "/etc/secrets/dev/secretkey.txt";
    private static final String KEY_ALGORITHM = "RSA";

    private static PrivateKey loadPrivateKey(@NonNull String path) throws Exception {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            var keyContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8).replaceAll("\\s+", "");
            byte[] decodedKey = Base64.getDecoder().decode(keyContent);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
            return kf.generatePrivate(spec);
        }
    }

    private static PublicKey loadPublicKey(@NonNull String path) throws Exception {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            var keyContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8).replaceAll("\\s+", "");
            byte[] decodedKey = Base64.getDecoder().decode(keyContent);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
            KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
            return kf.generatePublic(spec);
        }
    }

    private static String loadSecretKey(@NonNull String path) throws Exception {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8).replaceAll("\\s+", "");
        }
    }

    public static String generateAccessToken(String role) throws Exception {
        PrivateKey privateKey = loadPrivateKey(RSA_PRIVATE_KEY_PATH);
        Instant now = Instant.now();
        Instant exp = now.plus(1, ChronoUnit.HOURS);

        return Jwts.builder()
                .setHeaderParam("alg", "RS256")
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("role", role)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public static String generateRefreshToken(String role) throws Exception {
        String secretKey = loadSecretKey(HS256_SECRET_KEY_PATH);
        Instant now = Instant.now();
        Instant exp = now.plus(30, ChronoUnit.DAYS);

        return Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateAccessToken(String token) throws Exception {
        PrivateKey privateKey = loadPrivateKey(RSA_PRIVATE_KEY_PATH);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(privateKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new Exception("Invalid access token signature");
        }
    }

    public static Claims validateRefreshToken(String token) throws Exception {
        String secretKey = loadSecretKey(HS256_SECRET_KEY_PATH);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new Exception("Invalid refresh token signature");
        }
    }

    public static String extractTokenFromRequest(HttpServletRequest request) throws Exception {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new Exception("extractTokenFromRequest is null");
    }

    public static List<String> validateAndExtractRolesFromToken(HttpServletRequest request) throws Exception {
        var token = extractTokenFromRequest(request);
        Claims claims = validateAccessToken(token);
        return List.of(claims.get("role", String.class));
    }

}