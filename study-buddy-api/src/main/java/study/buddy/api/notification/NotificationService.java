package study.buddy.api.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    public Optional<Notification> findById(Long id){ return notificationRepository.findById(id); }
    public List<Notification> findByRecipient(String recipient){ return notificationRepository.findByRecipient(recipient); }
    public List<Notification> findBySessionID(Long sessID){ return notificationRepository.findBySessionIDAndTypeIsNot(sessID, Notification.NotifType.RATE_TUTOR); }
    public boolean hasUnreadNotifs(String recipient){return notificationRepository.existsByRecipientAndReadFalse(recipient); }

    public Notification saveNotification(Notification s){ return notificationRepository.save(s); }
    public void deleteById(Long id){ notificationRepository.deleteById(id); }
}
