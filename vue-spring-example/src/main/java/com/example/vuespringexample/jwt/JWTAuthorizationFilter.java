package com.example.vuespringexample.jwt;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.example.vuespringexample.jwt.JWTConstant.HEADER_STRING_AUTHORIZATION;
import static com.example.vuespringexample.jwt.JWTConstant.SECRET;
import static com.example.vuespringexample.jwt.JWTConstant.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        String header = request.getHeader(HEADER_STRING_AUTHORIZATION);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            // 処理を中止し次のフィルターへ
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken token = extractAuthenticationToken(request);

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken extractAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING_AUTHORIZATION);

        if (token == null) {
            return null;
        }

        String user = JWT
            .require(Algorithm.HMAC512(SECRET))
            .build()
            .verify(token.replace(TOKEN_PREFIX + " ", ""))
            .getSubject();

        if (user == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }
}
