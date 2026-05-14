package com.project.moneymanager.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtil {
    @Value("${jwt_secret}")
    private String jwt_secret;
    private Key key;
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwt_secret.getBytes());
    }
    private final long time=1000*60*60*24*30L;
    public String generateToken(String email)
    {
        System.out.println("Current Time: " + new Date());
        System.out.println("Expiry Time: " + new Date(System.currentTimeMillis()+time));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .signWith(key)
                .compact();
    }

    public String extractUserName(String jwt)
    {
       return extractClaims(jwt,claims->claims.getSubject());
    }

    public <T> T extractClaims(String token, Function<Claims,T> resolver){
        Claims cliams= Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return resolver.apply(cliams);
    }

    public boolean validate(String jwt, UserDetails userDetails) {
        String name=extractUserName(jwt);
        return name.equals(userDetails.getUsername()) && isValid(jwt);
    }

    public boolean isValid(String token){

        Date date=extractClaims(token,claims->claims.getExpiration());
        System.out.println("Token Expiry: " + date);
        System.out.println("Server Current Time: " + new Date());
        return date.after(new Date());
    }
}
