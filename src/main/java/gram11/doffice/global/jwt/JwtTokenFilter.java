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
import org.springframework.web.filter.OncePerRequestFilter;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = jwtTokenProvider.getJwt(request);

        try {
            if (jwt != null) {
                Claims claims = jwtTokenProvider.parse(jwt);
                String tokenType = claims.get("tokenType", String.class);
                String username = claims.getSubject();
                Long userId = claims.get("userId", Long.class);
                String authoritiesStr = claims.get("authorities", String.class);

                if ("ACCESS".equals(tokenType) && username != null && userId != null) {
                    if (jwtTokenProvider.isBlackList(jwt)) {
                        throw InvalidJwtException.EXCEPTION;
                    }

                    List<GrantedAuthority> authorities = Arrays.stream(authoritiesStr.split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UserDetails userDetails = new CustomUserDetails(userId, username, authorities);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            throw ExpiredJwtException.EXCEPTION;
        } catch (InvalidJwtException e) {
            throw InvalidJwtException.EXCEPTION;
        } catch (Exception e) {
            throw e;
        }
    }
}