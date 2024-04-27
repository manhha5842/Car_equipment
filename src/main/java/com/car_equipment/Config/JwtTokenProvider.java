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

    // Chú ý thay thế "yourSecretKey" bằng secret key bảo mật thực sự và lưu trữ nó một cách an toàn
    @Value("${jwt.secret-key}")
    private String secretKey;

    // Thời gian hết hạn token
    @Value("${security.jwt.token.expire-length:3600000}") // 1h in milliseconds by default
    private long validityInMilliseconds;

    // Tạo JWT token
    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
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
            // Tùy vào nhu cầu, bạn có thể handle các exception cụ thể như ExpiredJwtException, UnsupportedJwtException, ...
            // Hiện tại chúng tôi sẽ catch mọi JwtException và IllegalArgumentException và trả về false
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

    // Lấy thông tin người dùng từ JWT token
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
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
        // Giả sử "role" là một String ngăn cách bởi dấu phẩy
        String roleStr = getUsername(token); // Giả sử bạn lưu trữ roles như là claim "auth" hoặc tương tự
        List<SimpleGrantedAuthority> authorities =
                Arrays.stream(roleStr.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User();
        principal.setEmail(getUsername(token));
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}