package com.example.demo.config;

import com.example.demo.Role;
import com.example.demo.token.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// BasicAuthenticationFilter - built-in Spring Security filter used to process HTTP basic authentication.
// This class is used to authenticate incoming HTTP requests with JWT tokens.
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final TokenProvider tokenProvider = new TokenProvider();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws IOException, ServletException {
        String jwt = this.resolveToken(request);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Jws<Claims> claims;
        try {
            claims = this.tokenProvider.parse(jwt);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String email = claims.getBody().get("email", String.class);
        Role role = Role.getByName(claims.getBody().get("role", String.class));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, List.of(role));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
    // checks the Authorization header for a Bearer token and returns it if found, otherwise it returns null.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
