package gram11.doffice.global.jwt;

import gram11.doffice.domain.auth.domain.RefreshToken;
import gram11.doffice.domain.auth.domain.repository.RefreshTokenRepository;
import gram11.doffice.domain.user.domain.User;
import gram11.doffice.domain.user.domain.repository.UserRepository;
import gram11.doffice.domain.user.exception.UserNotFoundException;
import gram11.doffice.global.config.auth.CustomUserDetails;
import gram11.doffice.global.jwt.exception.ExpiredJwtException;
import gram11.doffice.global.jwt.exception.InvalidJwtException;
import gram11.doffice.global.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final RedisService redisService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            RedisService redisService,
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisService = redisService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public String generateToken(CustomUserDetails customUserDetails, String type, Long ext) {
        LocalDateTime now = LocalDateTime.now();

        Date expiresAt = Date.from(now.plusSeconds(ext)
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
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateAccessToken(CustomUserDetails customUserDetails) {
        return generateToken(customUserDetails, "ACCESS", accessTokenExpiration);
    }

    public String generateRefreshToken(CustomUserDetails customUserDetails) {
        String refreshToken = generateToken(customUserDetails, "REFRESH", refreshTokenExpiration);

        String key = "RT:" + customUserDetails.getUsername();
        redisService.set(
                key,
                refreshToken,
                refreshTokenExpiration
        );

        return refreshToken;
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
            throw InvalidJwtException.EXCEPTION;
        }
    }

    public boolean validateToken(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String reissueAccessToken(String refreshToken) {
        String username = getUsername(refreshToken);
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken);

        if (!storedToken.getUsername().equals(username)) {
            throw InvalidJwtException.EXCEPTION;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        return generateAccessToken(customUserDetails);
    }

    public String reissueRefreshToken(String oldRefreshToken) {
        String username = getUsername(oldRefreshToken);

        // Redis에서 기존 토큰 확인
        RefreshToken storedToken = refreshTokenRepository.findByToken(oldRefreshToken);

        // username 일치 확인
        if (!storedToken.getUsername().equals(username)) {
            throw InvalidJwtException.EXCEPTION;
        }

        // 기존 Refresh Token 삭제
        refreshTokenRepository.delete(storedToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 새로운 Refresh Token 발급
        return generateRefreshToken(customUserDetails);
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

    public String getRefreshToken(String username) {
        Object token = redisService.get("RT:" + username);
        return (token != null) ? token.toString() : null;
    }

    public long getExpiration(String token) {
        Claims claims = parse(token);
        Date expiration = claims.getExpiration();
        long nowMillis = Instant.now().toEpochMilli();

        long remainTimeMillis = expiration.getTime() - nowMillis;

        if (remainTimeMillis > 0) {
            return remainTimeMillis / 1000;
        }
        return 0;
    }

    // Jwt 추출 메서드
    public String getJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return null;
        }

        return bearerToken.substring(7);
    }

    // redis 관련

    public boolean isBlackList(String accessToken) {
        return redisService.get("BlackList:" + accessToken) != null;
    }

    public void addToBlackList(String accessToken, String username, long expiration) {
        String blackListKey = "BlackList:" + accessToken;
        redisService.set(blackListKey, username, expiration);
    }

    public void deleteRefreshToken(String username) {
        // 키 생성 규칙(RT:) 및 Redis 접근 로직을 Provider가 캡슐화
        redisService.delete("RT:" + username);
    }

    public void saveRefreshToken(String username, String refreshToken) {
        String key = "RT:" + username;
        redisService.set(key, refreshToken, refreshTokenExpiration);
    }
}
