package com.mapaware.Jwt;

import com.mapaware.service.UserDetailsServiceImpl;
import com.mapaware.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
@RequiredArgsConstructor

@Service
public class JwtService {


    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;


//    @Value("${jwt.secret}")
    private final String secretKey = "pMe49rDiYgX+WaBeQSDNqikgekcqYZAQB1Vi3iIWDLnIoHVqyP1aBAt2rmh56aNXfNQAvsK4zdW4QGVBOY0sbOklnzjaXnuHgIBH8BdiHRxLw248+Lg/xlw7QWLM0VXqkWYmWv3mwd9+c6a4H00Zp62Q8ko29LadP10KyYo6/RVU6EMTDK8hKz06YIG01jN0cYhBdIA9PW1IWevQBAh17sQr/tfeCD/fsO3xposfu3I8B7QkQyoIdWWeSYozHROjHXSk6FtW4Ci/2t7HFLBh4Y8ez0fsSWrkdZ4t9Zge2sj5RWpjX8KDcq0XI9s0AoZeaahRW1ygE4jO8CRApR1Rbg==";

//    @Value("${jwt.expiration}")
    private final int expirationTime = 86400000;

    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", getRolesFromUser(user));

        return Jwts.builder()
                .setClaims(claims)
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
//            return true;
        } catch (ExpiredJwtException | SignatureException e) {
            return false;
        }
    }

    private Collection<String> getRolesFromUser(UserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    //-----------------------------------------

    public UserDetails extractUserDetails(String token) {
//        System.out.println("JWT SERVICE EXTRACT: "+token);
        String username = getUsernameFromToken(token);
//        System.out.println("JWT SERVICE USERNAME:"+username);
        return userDetailsService.loadUserByUsername(username);
    }

    private String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//            System.out.println(claims.getSubject());
            return claims.getSubject();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Unable to extract username from token.");
        }
    }
}
