package gram11.doffice.global.jwt;

import gram11.doffice.global.config.auth.CustomUserDetails;
import gram11.doffice.global.jwt.exception.ExpiredJwtException;
import gram11.doffice.global.jwt.exception.InvalidJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateToken(CustomUserDetails customUserDetails, String type, Long ext) {
        LocalDateTime now = LocalDateTime.now();

        Date expiresAt = Date.from(now.plusSeconds(ext/1000)
                .atZone(ZoneId.systemDefault()).toInstant());

        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .claim("userId", customUserDetails.getId())
                .claim("authorities", authorities)
                .claim("tokenType", type)
                .issuedAt(new Date())
                .expiration(expiresAt)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public String generateAccessToken(CustomUserDetails customUserDetails) {
        return generateToken(customUserDetails, "ACCESS", accessTokenExpiration);
    }

    public String generateRefreshToken(CustomUserDetails customUserDetails) {
        return generateToken(customUserDetails, "REFRESH", refreshTokenExpiration);
    }

    // Token Body를 얻는 메서드
    public Claims parse(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.ExpiredJwtException e){
            throw ExpiredJwtException.EXCEPTION;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            // 2. 그 외 모든 유효성 예외(서명, 형식 오류, null 등)는 Invalid로 처리합니다.
            throw InvalidJwtException.EXCEPTION;
        }
    }

    public String getUsername(String token) {
        return parse(token).getSubject();
    }

    public Long getUserId(String token) {
        return parse(token).get("userId", Long.class);
    }

    public String getTokenType(String token) {
        return parse(token).get("tokenType", String.class);
    }
}
