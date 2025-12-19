package gram11.doffice.domain.auth.presentation.dto;

import gram11.doffice.global.config.auth.CustomUserDetails;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        CustomUserDetails customUserDetails
) {
}