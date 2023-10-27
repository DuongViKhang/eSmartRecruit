package com.example.eSmartRecruit.config;

import com.example.eSmartRecruit.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    //private static final String SECRET_KEY = "MH6y6TonkU28hwULmE1W3lYWEPHACHxh";
    private static final String SECRET_KEY = "d2a515e262360c96d3c667e91d58489de5c1f18ae456d9c6b1a5a624a8f5a2fc";


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token){
        return extractClaim(token,Claims::getId);
    }

    public String generateToken(Users user){
        return generateToken(new HashMap<>(), user);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String email = extractUserName(token);
        return (email.equals(userDetails.getUsername()) && !isTokenexpired(token));
    }

    public boolean isTokenexpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            Users users
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(users.getRoleName().name())
                .setId(users.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Claims extractAllClaims(String token){
        return Jwts.
                parserBuilder().
                setSigningKey(getSignKey()).
                build().
                parseClaimsJws(token).
                getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
