// campuslk-api/src/main/java/lk/campuslk/auth/dto/RegisterRequest.java
package lk.campuslk.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password,
        String universityId,
        String fieldOfStudy,
        String interests
) {}
