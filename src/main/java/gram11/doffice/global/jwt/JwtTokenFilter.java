package gram11.doffice.global.jwt;

import gram11.doffice.global.config.auth.CustomUserDetails;
import gram11.doffice.global.jwt.exception.ExpiredJwtException;
import gram11.doffice.global.jwt.exception.InvalidJwtException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter: 상속받은 클래스가 해당 필터를 한 번 실행할 수 있도록 함
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher matcher = new AntPathMatcher(); // url, 파일 경로가 일치하는 지 확인하는 Matcher

    // TODO: 안에 들어갈 end point 명시하기, 귀찮아서 미룸
    private static final String[] PERMITTED_AUTH = {
            "/auth/**"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // OPTIONS 요청은 CORS preflight 이므로 필터링 제외, 라는데 공부가 더 필요할 거 같다.
        if ("OPTIONS".equals(method)) {
            return true;
        }

        return Arrays.stream(PERMITTED_AUTH)
                .anyMatch(permit -> matcher.match(permit, path));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, // HTTP request를 담고 있는 클래스
                                    @NonNull HttpServletResponse response, // HTTP response를 담는 클래스
                                    @NonNull FilterChain chain // Spring의 Filter들을 체인처럼 연결해 놓은 클래스
                                    ) throws ServletException, IOException {
        String jwt = getJwt(request);

        if (jwt == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 파싱 및 유효성 검사
            Claims claims = jwtTokenProvider.parse(jwt);

            String tokenType = claims.get("tokenType", String.class);
            String username = claims.getSubject();
            Long userId = claims.get("userId", Long.class);
            String authoritiesStr = claims.get("authorities", String.class);

            // ACCESS 토큰이고 필수 클레임이 존재할 경우
            if ("ACCESS".equals(tokenType) && username != null && userId != null) {

                // 권한 파싱
                List<GrantedAuthority> authorities = Arrays.stream(authoritiesStr.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UserDetails userDetails = new CustomUserDetails(userId, username, authorities);

                // 인증 객체 생성 및 Security Context에 설정
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            throw ExpiredJwtException.EXCEPTION;
        } catch (InvalidJwtException e) {
            throw InvalidJwtException.EXCEPTION;
        }
    }

    // Jwt 추출 메서드
    private String getJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw InvalidJwtException.EXCEPTION;
        }

        return bearerToken.substring(7);
    }
}
