package gram11.doffice.domain.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "{validation.login.username}")
        String username,

        @NotBlank(message = "{validation.login.password}")
        String password
) {
}