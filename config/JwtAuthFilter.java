// campuslk-api/src/main/java/lk/campuslk/config/JwtAuthFilter.java
package lk.campuslk.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.campuslk.db.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Reads: Authorization: Bearer <token>
 * If token valid -> sets authenticated principal = studentId (subject)
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final StudentRepo studentRepo;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();
        if (token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // subject = studentId
            String studentId = jwtService.parseSubject(token);

            // If already authenticated, skip
            if (studentId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // optional: check student exists in DB
                boolean exists = studentRepo.existsById(studentId);
                if (exists) {
                    var auth = new UsernamePasswordAuthenticationToken(
                            studentId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ignored) {
            // If token invalid/expired -> do nothing, request continues unauthenticated
            // SecurityConfig will block protected endpoints automatically.
        }

        filterChain.doFilter(request, response);
    }
}
