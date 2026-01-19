// campuslk-api/src/main/java/lk/campuslk/auth/dto/LoginRequest.java
package lk.campuslk.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password
) {}
