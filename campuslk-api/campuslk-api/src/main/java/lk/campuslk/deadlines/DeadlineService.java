package lk.campuslk.deadlines;

import lk.campuslk.db.entity.Bookmark;
import lk.campuslk.db.entity.Deadline;
import lk.campuslk.db.repo.BookmarkRepo;
import lk.campuslk.db.repo.DeadlineRepo;
import lk.campuslk.notifications.NotificationService;
import lk.campuslk.notifications.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeadlineService {

    private final DeadlineRepo deadlineRepo;
    private final BookmarkRepo bookmarkRepo;
    private final NotificationService notificationService;

    @Transactional
    public Deadline save(Deadline d) {
        Deadline saved = deadlineRepo.save(d);

        // Notify bookmarked students about the new deadline
        try {
            if (saved.getOpportunity() != null && saved.getOpportunity().getId() != null) {
                List<Bookmark> bookmarks = bookmarkRepo.findByOpportunityId(saved.getOpportunity().getId());
                for (Bookmark b : bookmarks) {
                    if (b.getStudent() == null) continue;
                    NotificationDto dto = new NotificationDto();
                    dto.setTitle("Deadline: " + (saved.getOpportunity().getTitle() == null ? "" : saved.getOpportunity().getTitle()));
                    dto.setBody("Due date: " + (saved.getDueDate() == null ? "" : saved.getDueDate().toString()));
                    dto.setCategory("deadline");
                    dto.setTargetStudentId(b.getStudent().getId());
                    notificationService.createNotification(dto);
                }
            }
        } catch (Exception ignored) {
            // Don't fail save if notifications fail
        }

        return saved;
    }

    // Run daily at 09:00 to send reminders for deadlines due tomorrow
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void sendUpcomingReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Deadline> pending = deadlineRepo.findByReminderSentFalse();
        for (Deadline d : pending) {
            if (d.getDueDate() != null && d.getDueDate().isEqual(tomorrow)) {
                try {
                    List<Bookmark> bookmarks = bookmarkRepo.findByOpportunityId(d.getOpportunity().getId());
                    for (Bookmark b : bookmarks) {
                        if (b.getStudent() == null) continue;
                        NotificationDto dto = new NotificationDto();
                        dto.setTitle("Upcoming deadline: " + (d.getOpportunity().getTitle() == null ? "" : d.getOpportunity().getTitle()));
                        dto.setBody("Due tomorrow: " + d.getDueDate().toString());
                        dto.setCategory("deadline_reminder");
                        dto.setTargetStudentId(b.getStudent().getId());
                        notificationService.createNotification(dto);
                    }
                } catch (Exception ignored) {
                }

                d.setReminderSent(true);
                deadlineRepo.save(d);
            }
        }
    }
}

