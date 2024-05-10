package com.mapaware.Jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
@RequiredArgsConstructor

@Service
public class JwtService {


    private final UserDetailsService userDetailsService;


//    @Value("${jwt.secret}")
    private final String secretKey = "b/CKlCiuLODz0VojQ6ahAeV7BC0hPegehzP7iYmdd08=";

//    @Value("${jwt.expiration}")
    private final int expirationTime = 86400000;

    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", getRolesFromUser(user));

        return Jwts.builder()
//                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException | SignatureException e) {
            return false;
        }
    }

    private Collection<String> getRolesFromUser(UserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public UserDetails extractUserDetails(String token) {
        System.out.println(token);
        String username = getUsernameFromToken("JWT SERVICE EXTRACT: "+token);
        return userDetailsService.loadUserByUsername(username);
    }

    private String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Unable to extract username from token.");
        }
    }
}
