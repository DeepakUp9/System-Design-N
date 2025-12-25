package com.espn.cricinfo.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT utility class for token generation and validation.
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${cricinfo.security.jwt.secret}")
    private String jwtSecret;

    @Value("${cricinfo.security.jwt.expiration}")
    private int jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generate JWT token for user.
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    /**
     * Generate JWT token from username.
     */
    public String generateTokenFromUsername(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Generate refresh token.
     */
    public String generateRefreshToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        return Jwts.builder()
                .claims(claims)
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs * 7)) // 7 days
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Get username from JWT token.
     */
    public String getUserNameFromJwtToken(String token) {
        return getClaimFromJwtToken(token, Claims::getSubject);
    }

    /**
     * Get token type from JWT token.
     */
    public String getTokenTypeFromJwtToken(String token) {
        return getClaimFromJwtToken(token, claims -> claims.get("type", String.class));
    }

    /**
     * Extract claim from JWT token.
     */
    public <T> T getClaimFromJwtToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromJwtToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get all claims from JWT token.
     */
    private Claims getAllClaimsFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Validate JWT token.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Check if token is expired.
     */
    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromJwtToken(token);
        return expiration.before(new Date());
    }

    /**
     * Get expiration date from JWT token.
     */
    public Date getExpirationDateFromJwtToken(String token) {
        return getClaimFromJwtToken(token, Claims::getExpiration);
    }

    /**
     * Get issued at date from JWT token.
     */
    public Date getIssuedAtDateFromJwtToken(String token) {
        return getClaimFromJwtToken(token, Claims::getIssuedAt);
    }
}
