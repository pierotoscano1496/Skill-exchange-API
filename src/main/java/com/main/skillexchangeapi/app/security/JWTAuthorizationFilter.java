package com.main.skillexchangeapi.app.security;

import com.main.skillexchangeapi.domain.abstractions.services.ITokenBlackList;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private ITokenBlackList tokenBlackList;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        String token = tokenUtils.extractTokenFromRequest(request);

        // Imprimir cabeceras de la petición
        Collections.list(request.getHeaderNames()).stream().forEach(headerName -> {
            System.out.println("Header: " + headerName + ", Value: " + request.getHeader(headerName));
        });

        if (bearerToken != null && bearerToken.startsWith("Bearer ") && !tokenBlackList.isBlacklisted(token)) {
            UsernamePasswordAuthenticationToken usernamePAT = tokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(usernamePAT);
        }

        filterChain.doFilter(request, response);
    }


}
