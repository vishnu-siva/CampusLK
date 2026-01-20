package lk.campuslk.deadlines;

import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.Deadline;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deadlines")
@RequiredArgsConstructor
public class DeadlineController {

    private final DeadlineService deadlineService;

    @PostMapping
    public ApiResponse<Deadline> save(@RequestBody Deadline d) {
        return new ApiResponse<>(true, "Saved", deadlineService.save(d));
    }

    // Manual trigger for testing scheduled reminders
    @PostMapping("/trigger-reminders")
    public ApiResponse<Void> triggerReminders() {
        deadlineService.sendUpcomingReminders();
        return new ApiResponse<>(true, "Reminders triggered", null);
    }
}
