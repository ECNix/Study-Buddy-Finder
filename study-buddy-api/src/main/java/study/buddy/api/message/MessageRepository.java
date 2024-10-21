package study.buddy.api.message;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<RecipientOnly> findDistinctByRecipient(String recipient);
    List<SenderOnly> findDistinctBySender(String sender);
    List<Message> findByRecipientOrderByDateCreated(String recipient);
    List<Message> findBySenderOrderByDateCreated(String sender);
    List<Message> findByRecipientAndSenderOrderByDateCreated(String recipient, String sender);
    @Query(value = "SELECT COUNT(*) FROM message WHERE SENDER=?1 AND RECIPIENT=?2 AND DATE_VIEWED IS NULL", nativeQuery = true)
    Long countUnread(String sender, String recipient);
    @Query(value = "SELECT COUNT(*) FROM message WHERE RECIPIENT=?1 AND DATE_VIEWED IS NULL", nativeQuery = true)
    Long countAllUnread(String sender);
    @Modifying
    @Query(value = "UPDATE message SET DATE_VIEWED=?3 WHERE SENDER=?1 AND RECIPIENT=?2", nativeQuery = true)
    void markRead(String sender, String recipient, GregorianCalendar now);

    @Query(value = "SELECT * FROM message WHERE RECIPIENT=?1 OR SENDER=?1 ORDER BY DATE_CREATED LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Message> getMessages(String username, long numEntries, long offset);

    @Query(value = "SELECT * FROM message WHERE (RECIPIENT=?1 AND SENDER=?2) OR (RECIPIENT=?2 AND SENDER=?1) ORDER BY DATE_CREATED DESC LIMIT ?3 OFFSET ?4", nativeQuery = true)
    List<Message> getMessages(String username, String other, long numEntries, long offset);

    @Query(value = "SELECT COUNT(*) FROM message WHERE (RECIPIENT=?1 AND SENDER=?2) OR (RECIPIENT=?2 AND SENDER=?1)", nativeQuery = true)
    Long getNumMessages(String username, String other);

    @Modifying
    void deleteById(Long id);
//    @Query(value = "delete from MESSAGE b where b.title=?", nativeQuery = true)
//    void clearMessages(String title);
}
