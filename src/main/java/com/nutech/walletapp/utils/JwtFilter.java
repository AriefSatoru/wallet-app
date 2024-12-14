package com.nutech.walletapp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.getPayloadToken(token);
                    request.setAttribute("claims", claims);
                    return true;
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token tidak tidak valid atau kadaluwarsa");
                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token tidak tidak valid atau kadaluwarsa");
            }
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or invalid");
        return false;
    }
}
