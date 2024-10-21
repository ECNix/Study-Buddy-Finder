package study.buddy.api.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    public List<Message> getMyMessages(String recipient) {
        return messageRepository.findByRecipientOrderByDateCreated(recipient);
    }
    public List<Message> getSentMessages(String sender) {
        return messageRepository.findBySenderOrderByDateCreated(sender);
    }
    public List<Message> getEntireMessageHistory(String recipient, String sender) {
        return messageRepository.findByRecipientAndSenderOrderByDateCreated(recipient, sender);
    }

    public List<Message> getMessages(String username, Long limit, Long offset) {
        return messageRepository.getMessages(username, limit, offset);
    }
    public Long getNumMessages(String username, String other) {
        return messageRepository.getNumMessages(username, other);
    }
    public List<Message> getMessages(String username, String other, Long limit, Long offset) {
        return messageRepository.getMessages(username, other, limit, offset);
    }

    public Message createMessage(String sender, String recipient, String contents) {
        Message message = new Message(sender, recipient, contents);
        return messageRepository.save(message);
    }

    public Long checkUnread(String sender, String recipient) {
        return messageRepository.countUnread(sender, recipient);
    }
    public Long checkAllUnread(String recipient) {
        return messageRepository.countAllUnread(recipient);
    }

    @Transactional
    public void markRead(String sender, String recipient) {
        messageRepository.markRead(sender, recipient, new GregorianCalendar());
    }

}
