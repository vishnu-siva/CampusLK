// campuslk-api/src/main/java/lk/campuslk/auth/AuthService.java
package lk.campuslk.auth;

import lk.campuslk.auth.dto.*;
import lk.campuslk.config.JwtService;
import lk.campuslk.db.entity.Student;
import lk.campuslk.db.entity.University;
import lk.campuslk.db.repo.StudentRepo;
import lk.campuslk.db.repo.UniversityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepo studentRepo;
    private final UniversityRepo universityRepo;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthResponse register(RegisterRequest req) {
        studentRepo.findByEmail(req.email()).ifPresent(s -> {
            throw new IllegalArgumentException("Email already registered");
        });

        University uni = null;
        if (req.universityId() != null && !req.universityId().isBlank()) {
            uni = universityRepo.findById(req.universityId()).orElse(null);
        }

        var now = Instant.now();
        var student = Student.builder()
                .id(UUID.randomUUID().toString())
                .name(req.name())
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .fieldOfStudy(req.fieldOfStudy())
                .interests(req.interests())
                .createdAt(now)
                .updatedAt(now)
                .university(uni)
                .build();

        studentRepo.save(student);

        String token = jwtService.generateToken(student.getId());
        return new AuthResponse(token, student.getId(), student.getName(), student.getEmail());
    }

    public AuthResponse login(LoginRequest req) {
        var student = studentRepo.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!encoder.matches(req.password(), student.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(student.getId());
        return new AuthResponse(token, student.getId(), student.getName(), student.getEmail());
    }
}
