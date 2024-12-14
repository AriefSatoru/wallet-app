package com.nutech.walletapp.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                                    .setSigningKey(getSignKey())
                                    .build()
                                    .parseClaimsJws(token);
            
            // Cek apakah token sudah kadaluarsa
            if (claims.getBody().getExpiration().before(new Date())) {
                return false; // Token kadaluarsa
            }

            return true;
        } catch (SignatureException ex) {
            // Token ditandatangani dengan kunci yang salah
            return false;
        } catch (MalformedJwtException ex) {
            // Token memiliki format yang salah
            return false;
        } catch (ExpiredJwtException ex) {
            // Token sudah kadaluarsa
            return false;
        } catch (UnsupportedJwtException ex) {
            // Algoritma tanda tangan tidak didukung
            return false;
        } catch (IllegalArgumentException ex) {
            // Token kosong
            return false;
        }
    }

    public Claims getPayloadToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
