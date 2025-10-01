package com.main.skillexchangeapi.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.ITokenBlackList;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenUtils {
    private final String accessTokenSignature;
    private final static Long ACCESS_TOKEN_VALIDITY_MILI_SECONDS = 1000 * 60 * (long) 60 * 5; // 5 horas

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    public TokenUtils(@Value("${jwt.secret}") String accessTokenSignature) {
        this.accessTokenSignature = accessTokenSignature;
    }

    public String createToken(String nombre, String email) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_MILI_SECONDS;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("nombre", nombre);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(claims)
                .signWith(Keys.hmacShaKeyFor(accessTokenSignature.getBytes()))
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(accessTokenSignature.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException ex) {
            return null;
        }
    }

    public String extractEmailFromRequest(HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(accessTokenSignature.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException ex) {
            return null;
        }
    }

    public UUID extractIdFromRequest(HttpServletRequest request) {
        Logger logger = LoggerFactory.getLogger(TokenUtils.class);
        logger.info("Extracting user ID from request");
        String email = extractEmailFromRequest(request);
        if (email != null) {
            try {
                return usuarioRepository.obtenerByCorreo(email).getId();
            } catch (DatabaseNotWorkingException e) {
                logger.error("Database not working: {}", e.getMessage(), e);
            } catch (ResourceNotFoundException e) {
                logger.error("Resource not found: {}", e.getMessage(), e);
            }
        }
        return null;
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.replace("Bearer ", "");
        } else {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(accessTokenSignature.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(accessTokenSignature.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
