package study.buddy.api.endpoint;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import study.buddy.api.notification.Notification;
import study.buddy.api.notification.NotificationService;
import study.buddy.api.security.JWToken;
import study.buddy.api.session.SessionService;

import java.util.*;

@Log4j2
@RestController
public class NotificationEndpoint {
    @Autowired
    NotificationService notificationService;
    @Autowired
    SessionService sessionService;
    @Autowired
    JWToken jwToken;

    @GetMapping("/notifications/recipient")
    public List<Notification> getMyNotifications(@RequestHeader String Authorization){
        String sender = jwToken.getSubject(Authorization);
        return notificationService.findByRecipient(sender);
    }
    @GetMapping("/notifications/id")
    public Map<String, Object> getSpecificNotification(@RequestHeader String Authorization, @RequestParam Long id){
        String recip = jwToken.getSubject(Authorization);
        Optional<Notification> not = notificationService.findById(id);

        if(not.isPresent() && not.get().getRecipient().equals(recip)) {

            Optional<Notification> notification = notificationService.findById(id);

            if(notification.isPresent()) {
                return notification.get().toMap();
            }
        }
        return new HashMap<>();
    }
    @GetMapping("/notifications/unread")
    public boolean getUnreadNotifications(@RequestHeader String Authorization){
        String sender = jwToken.getSubject(Authorization);
        boolean hasNotif = notificationService.hasUnreadNotifs(sender);
        System.out.println("Notif: " + hasNotif);
        return hasNotif;
    }

    @PostMapping("/notifications/read")
    public Map<String, Object> markRead(@RequestHeader String Authorization, @RequestBody Map<String, String> m){
        String recip = jwToken.getSubject(Authorization);
        Optional<Notification> not = notificationService.findById(Long.parseLong(m.get("id")));

        if(not.isPresent() && not.get().getRecipient().equals(recip)){
            not.get().setRead(true);
            return notificationService.saveNotification(not.get()).toMap();
        }
        else return new HashMap<>();
    }
    @PostMapping("/notifications/save")
    public Notification saveSession(@RequestHeader String Authorization, @RequestBody Map<String, String> m){
        String sender = jwToken.getSubject(Authorization);

        //sessID, username
        Notification.NotifType type = Notification.NotifType.valueOf(m.get("type"));
        String message = "There has been a mistake";
        String sessName = "";
        if(m.get("sessID") != null)
            sessName = sessionService.findSessionByID(Long.parseLong(m.get("sessID"))).get().getName();
        switch (type){
            case SESSION_INVITE -> message = sender + " has requested you join session " + sessName;
            case FRIEND_REQUEST -> message = sender + " has sent you a friend request";
            case JOINED_SESSION -> message = sender + " has joined " + sessName;
            case UPCOMING_SESSION -> message = sessName + " is starting soon";
            case UNREAD_MESSAGE -> message = "You have an unread message from " + sender;
        }
        Notification not = new Notification(
            m.get("recipient"),
            sender,
            m.get("sessID") != null ? Long.parseLong(m.get("sessID")) : null,
            type, message
        );
        return notificationService.saveNotification(not);
    }
    @PostMapping("/notifications/delete")
    public Long deleteNotification(@RequestHeader String Authorization, @RequestBody Map<String, String> m){
        String recip = jwToken.getSubject(Authorization);
        Optional<Notification> not = notificationService.findById(Long.parseLong(m.get("id")));
        Long result = 0L;
        if(not.isPresent() && not.get().getRecipient().equals(recip)) {
            notificationService.deleteById(Long.parseLong(m.get("id")));
            result = 1L;
        }
        return result;
    }
}
