// ...existing code...
package lk.campuslk.notifications;

import lk.campuslk.db.entity.Notification;
import lk.campuslk.db.repo.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepo repo;

    // map of studentId -> list of emitters. Use "PUBLIC" for broadcast subscribers
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public Notification save(Notification n) {
        if (n.getCreatedAt() == null) n.setCreatedAt(Instant.now());
        return repo.save(n);
    }

    public List<Notification> listForStudent(String studentId) {
        List<Notification> broadcast = repo.findByTargetStudentIdIsNullOrderByCreatedAtDesc();
        if (studentId == null) {
            return broadcast;
        }

        List<Notification> personal = repo.findByTargetStudentIdOrderByCreatedAtDesc(studentId);
        List<Notification> all = new ArrayList<>(broadcast.size() + personal.size());
        all.addAll(broadcast);
        all.addAll(personal);

        // Keep a consistent newest-first order across the combined list
        all.sort(Comparator.comparing(Notification::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .reversed());
        return all;
    }

    public Optional<Notification> markRead(String id) {
        Optional<Notification> opt = repo.findById(id);
        opt.ifPresent(n -> {
            n.setRead(true);
            repo.save(n);
        });
        return opt;
    }

    public SseEmitter subscribe(String studentId) {
        String key = (studentId == null) ? "PUBLIC" : studentId;
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.computeIfAbsent(key, k -> Collections.synchronizedList(new ArrayList<>())).add(emitter);

        emitter.onCompletion(() -> removeEmitter(key, emitter));
        emitter.onTimeout(() -> removeEmitter(key, emitter));
        emitter.onError(e -> removeEmitter(key, emitter));

        return emitter;
    }

    private void removeEmitter(String key, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(key);
        if (list != null) {
            list.remove(emitter);
        }
    }

    public void publish(Notification n) {
        // persist
        Notification saved = save(n);
        // send to target student if present
        if (saved.getTargetStudentId() != null) {
            sendToKey(saved.getTargetStudentId(), saved);
        } else {
            // broadcast to public subscribers
            sendToKey("PUBLIC", saved);
            // also send to all individual subscribers
            emitters.forEach((key, list) -> {
                if (!"PUBLIC".equals(key)) sendToKey(key, saved);
            });
        }
    }

    private void sendToKey(String key, Notification payload) {
        List<SseEmitter> list = emitters.get(key);
        if (list == null) return;
        List<SseEmitter> copy;
        synchronized (list) { copy = new ArrayList<>(list); }
        for (SseEmitter emitter : copy) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(payload));
            } catch (IOException e) {
                removeEmitter(key, emitter);
            }
        }
    }
}

