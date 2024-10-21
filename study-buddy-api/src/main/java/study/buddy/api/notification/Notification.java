package study.buddy.api.notification;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = Notification.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = { "RECIPIENT", "MESSAGE", "SESSION_ID" }) })
public class Notification{
    public static final String TABLE_NAME = "NOTIFICATION";

    @Id
    @GeneratedValue(generator = TABLE_NAME + "_GENERATOR")
    @SequenceGenerator(
            name = TABLE_NAME + "_GENERATOR",
            sequenceName = TABLE_NAME + "_SEQUENCE"
    )
    @Column(name = "ID")
    @Getter Long id;

    @Column(name = "RECIPIENT")
    private @Getter String recipient;

    @Column(name = "USER")
    private @Getter String username;

    @Column(name = "SESSION_ID")
    private @Getter Long sessionID;

    @Column(name = "IS_READ")
    private @Getter boolean read;

    @Column(name = "MESSAGE")
    private @Getter String message;

    @Column(name = "TYPE")
    private @Getter NotifType type;

    public Notification(String r, String u, Long s, NotifType t, String m){
        recipient = r;
        username = u;
        sessionID = s;
        read = false;
        type = t;
        message = m;
    }

    public Notification() {
        recipient = "";
        username = "";
        sessionID = 0L;
        read = false;
        message = "There's been a mistake";
        type = NotifType.UNKNOWN;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("recipient", this.recipient);
        map.put("username", this.username);
        map.put("sessionID", this.sessionID);
        map.put("read", this.read);
        map.put("message", this.message);
        map.put("type", this.type);
        return map;
    }

    public enum NotifType{
        UNKNOWN,
        UPCOMING_SESSION,
        FRIEND_REQUEST,
        UNREAD_MESSAGE,
        JOINED_SESSION,
        RATE_TUTOR,

        SESSION_INVITE
    }
}


