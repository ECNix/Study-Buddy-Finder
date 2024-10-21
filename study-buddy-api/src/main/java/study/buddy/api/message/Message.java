package study.buddy.api.message;
import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import study.buddy.api.notification.Notification;

import java.util.GregorianCalendar;

@Data
@Entity
@Table(name = Message.TABLE_NAME)
public class Message {
    public static final String TABLE_NAME = "MESSAGE";

    @Id
    @GeneratedValue(generator = TABLE_NAME + "_GENERATOR")
    @SequenceGenerator(
            name = TABLE_NAME + "_GENERATOR",
            sequenceName = TABLE_NAME + "_SEQUENCE"
    )
    @Column(name = "ID")
    @Getter Long id;

    @Column(name = "RECIPIENT")
    @Getter String recipient;

    @Column(name = "SENDER")
    @Getter String sender;

    @Column(name = "CONTENTS")
    @Getter String contents;

    @Column(name = "DATE_CREATED")
    @Getter GregorianCalendar dateCreated;

    @Column(name = "DATE_VIEWED")
    @Getter GregorianCalendar dateViewed;

//    @Column(name = "NEXT")
//    @Getter Long next;

    public Message() {
    }

    public Message(String sender, String recipient, String contents) {
        this.recipient = recipient;
        this.sender = sender;
        this.contents = contents;
        this.dateCreated = new GregorianCalendar();
        this.dateViewed = null;
    }
}
