package study.buddy.api.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(String recipient);
    List<Notification> findBySessionIDAndTypeIsNot(Long sessionId, Notification.NotifType type);

    boolean existsByRecipientAndReadFalse(String recipient);
}
