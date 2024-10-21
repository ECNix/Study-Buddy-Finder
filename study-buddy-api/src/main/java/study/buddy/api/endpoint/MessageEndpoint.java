package study.buddy.api.endpoint;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import study.buddy.api.friend.Friend;
import study.buddy.api.friend.FriendService;

import org.springframework.web.bind.annotation.*;
import study.buddy.api.message.Message;
import study.buddy.api.message.MessageService;
import study.buddy.api.security.JWToken;
import study.buddy.api.study.Study;
import study.buddy.api.study.StudyService;
import study.buddy.api.user.User;
import study.buddy.api.user.UserService;

import java.util.*;

@Log4j2
@RestController
public class MessageEndpoint {
    @Autowired
    MessageService messageService;
    @Autowired
    JWToken jwToken;

    @PostMapping("/message/create")
    public Map<String, Object> createMessage(@RequestHeader String Authorization, @RequestBody Message message) {
        String sender = jwToken.getSubject(Authorization);
        Map<String, Object> result = new HashMap<>();
        if(!message.getSender().equals(sender)) {
            result.put("success", false);
            return result;
        }
        else {
            Message m = messageService.createMessage(sender, message.getRecipient(), message.getContents());
            result.put("result", m);
            result.put("success", true);

            return result;
        }
    }

    @GetMapping("/message/findall")
    public List<Message> findAllMessages(@RequestHeader String Authorization, @RequestParam Long limit, @RequestParam Long offset) {
        String username = jwToken.getSubject(Authorization);
        return messageService.getMessages(username, limit, offset);
    }

//    @GetMapping("/message/find")
//    public List<Message> findMessages(@RequestHeader String Authorization, @RequestParam String other, @RequestParam Long limit, @RequestParam Long offset) {
//        String username = jwToken.getSubject(Authorization);
//        List<Message> messages = messageService.getMessages(username, other, limit, offset);
//        return messages;
//    }

    @GetMapping("/message/find")
    public List<Message> findMessages(@RequestHeader String Authorization, @RequestParam String other, @RequestParam Long page, @RequestParam Long limit) {
        String username = jwToken.getSubject(Authorization);
        Long numMessages = messageService.getNumMessages(username, other);
        long totalPages = (numMessages / limit) + ((numMessages % limit > 0) ? 1 : 0);
        long currPage = Math.max(Math.min(page, totalPages-1), 0);
        List<Message> messages = messageService.getMessages(username, other, limit, currPage * limit);
        return messages;
    }

    @GetMapping("/message/unread")
    public Map<String, Object> getUnread(@RequestHeader String Authorization, @RequestParam String sender) {
        String recipient = jwToken.getSubject(Authorization);
        Long count = messageService.checkUnread(sender, recipient);
        Map<String, Object> result = new HashMap<>();
        if(count > 0) {
            result.put("unread", true);
        }
        else {
            result.put("unread", false);
        }

        return result;
    }
    @GetMapping("/message/checkunread")
    public Map<String, Object> checkUnread(@RequestHeader String Authorization) {
        String recipient = jwToken.getSubject(Authorization);
        Long count = messageService.checkAllUnread(recipient);
        Map<String, Object> result = new HashMap<>();
        if(count > 0) {
            result.put("unread", true);
        }
        else {
            result.put("unread", false);
        }

        return result;
    }

    @PostMapping("/message/markRead")
    public Long markRead(@RequestHeader String Authorization, @RequestBody Map<String, Object> request) {
        String recipient = jwToken.getSubject(Authorization);
        messageService.markRead(request.get("sender").toString(), recipient);
        return 1L;
    }
}
