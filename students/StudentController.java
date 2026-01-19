package lk.campuslk.students;

import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.Student;
import lk.campuslk.students.dto.StudentUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    /**
     * Get logged-in student profile
     */
    @GetMapping("/me")
    public ApiResponse<Student> me(Authentication auth) {
        String studentId = auth.getName(); // from JWT
        return new ApiResponse<>(true, "OK", service.getById(studentId));
    }

    /**
     * Update logged-in student profile
     */
    @PutMapping("/me")
    public ApiResponse<Student> update(
            Authentication auth,
            @RequestBody StudentUpdateRequest req
    ) {
        String studentId = auth.getName(); // from JWT
        return new ApiResponse<>(true, "Profile updated",
                service.update(studentId, req));
    }
}
