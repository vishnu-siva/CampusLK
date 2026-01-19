// campuslk-api/src/main/java/lk/campuslk/auth/dto/AuthResponse.java
package lk.campuslk.auth.dto;

public record AuthResponse(
        String token,
        String studentId,
        String name,
        String email
) {}
