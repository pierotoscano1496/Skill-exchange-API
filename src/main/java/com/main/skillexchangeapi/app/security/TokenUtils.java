package com.main.skillexchangeapi.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private final static String ACCESS_TOKEN = "_smFQI04ueQOV9El1r0ZxyYJT4ucYeLgTiOK2djlA13-oikO/VKz=Zrk!OV9B2uXzHJ?Ob=RF6MBLSXM?RAn2b7gwGwnuRHsDHWMriH4PP2ZMGYyLEvtxufjst7?wI0lg!PBQ4WXqTFIHyfDT!go6Dbhx/25rVUCBO!MFECFfKz-qWaqSLUFBS?sSz6QNDuB-Z=0tsZkOB7U?uirlMv=0Xx?DsCXErf8YzloSlA0j8?PuZ32gpX0p4q=exMGZfYc";
    private final static Long ACCESS_TOKEN_VALIDITY_MILI_SECONDS = 1000 * 60 * (long) 2; // 2 min

    public static String createToken(String nombre, String email) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_MILI_SECONDS;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("nombre", nombre);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(claims)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException ex) {
            return null;
        }
    }
}
