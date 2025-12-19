package gram11.doffice.domain.auth.service;

import gram11.doffice.domain.auth.presentation.dto.LoginRequest;
import gram11.doffice.domain.auth.presentation.dto.LoginResponse;
import gram11.doffice.global.config.auth.CustomUserDetails;
import gram11.doffice.global.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public LoginResponse Login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(customUserDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUserDetails);
        jwtTokenProvider.saveRefreshToken(customUserDetails.getUsername(), refreshToken);

        return new LoginResponse(accessToken, refreshToken, customUserDetails);
    }
}