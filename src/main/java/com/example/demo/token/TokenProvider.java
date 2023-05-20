package com.example.demo.token;

import com.example.demo.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

public class TokenProvider {
    public Key key = Keys.hmacShaKeyFor("'7V:lT@4sfsdterU6b~O(_nt5W0lJl@`wE".getBytes());

    public String generateJws(UserEntity user) {
        // JWT contains a claim with the user's email address, which can be used to identify the user.
        return Jwts.builder()
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("userID", user.getId())
                .setExpiration(Date.from(ZonedDateTime.now().plusHours(1).toInstant()))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String jws) {
        // given JWT returns a Jws object that contains the claims of the JWT, which can be used to verify the user's identity.
        JwtParser build = Jwts.parserBuilder().setSigningKey(key).build();
        return build.parseClaimsJws(jws);
    }
}
