package com.car_equipment.Config;

import com.car_equipment.Model.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    // Thời gian hết hạn token
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds;

    // Tạo JWT token
    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("auth", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Xác minh JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
//            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

    // Lấy thông tin người dùng từ JWT token
    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    public String getRole(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("auth").toString();
    }

    private static class InvalidJwtAuthenticationException extends RuntimeException {
        public InvalidJwtAuthenticationException(String explanation) {
            super(explanation);
        }
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        String roleStr = getRole(token);
        List<SimpleGrantedAuthority> authorities =
                Arrays.stream(roleStr.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(); // Tạo một đối tượng User mới mỗi lần gọi
        principal.setEmail(getEmail(token)); // Thiết lập thông tin email
        System.out.println(principal.toString());
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

}