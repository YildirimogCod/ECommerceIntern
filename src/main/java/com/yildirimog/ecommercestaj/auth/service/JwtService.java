package com.yildirimog.ecommercestaj.auth.service;

import com.yildirimog.ecommercestaj.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtService {
    //@Value("${jwt.secret}")
    //private String secret;
    @Value("${jwt.expirationInMillis}")
    private long jwtExpirationInMillis;
    private final SecretKey signingKey;

    //@PostConstruct
   // public void init() {
        // Neden: Eğer secret base64 encode edilmişse decode edilerek kullanılır. Aksi durumda hata alınır.
        // Eğer base64 değilse alternatif olarak getBytes(StandardCharsets.UTF_8) kullanılmalı.
     //   try {
      //      signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
       // } catch (IllegalArgumentException e) {
        //    log.error("JWT Secret key decoding failed. Ensure it is base64-encoded.", e);
         //   throw new RuntimeException("Invalid JWT secret key", e);
        //}
    //}
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtExpirationInMillis);

        // Neden: Daha esnek ve geliştirilebilir olması için claim'leri Map yapısıyla tanımlıyoruz.
        Map<String, Object> claims = Map.of(
                "role", user.getRole().name(), // Neden: Kullanıcının rolü burada yer alır
                "userId", user.getId()
        );

        return Jwts.builder()
                .subject(user.getUsername()) // Neden: Genellikle kullanıcı adı/email burada yer alır
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claims(claims)
                .signWith(signingKey)
                .compact();
    }
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public String extractUserRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
        }
        return false;
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.toInstant().isBefore(Instant.now());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
