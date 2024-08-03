package com.blog.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JWTHelper {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    private String secret = "afafafaffafafafafafafafafafa";

    //retrienve username from jwt token
    public String getUserNamefromToken(String token){
        return getClaimForToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token){
        return getClaimForToken(token, Claims::getExpiration);
    }

    public <T> T getClaimForToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need secret key
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token).getBody();
    }

    //check if token is expired
    private boolean isTokenExpired(String token){
        final Date expiration  = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    //generate token for user
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }
    //while creating the token
    //1. define claims of the token, like issuer,expiration ,subject and the id
    //2. signing the jwt using the HS512 algorithm and secret key
    //3. compaction of jwt to a url safe string

    private  String doGenerateToken(Map<String,Object> claims,String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date()).
                setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()),SignatureAlgorithm.ES512).compact();
    }

    public boolean validateToken(String token,UserDetails userDetails){
        final String userName = getUserNamefromToken(token);
        return (userName.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }
}
