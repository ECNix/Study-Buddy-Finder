package study.buddy.api.friend;


import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import study.buddy.api.user.UserID;



@Data
@Entity
@IdClass(UserID.class)
@Table(name = Friend.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = { "USERNAME", "FRIEND_USERNAME" }) })
public class Friend {
    public enum Status {
        SENT, ACCEPTED, DENIED, INVALID
    }

    public static final String TABLE_NAME = "FRIEND";

    @Id
    @GeneratedValue(generator = TABLE_NAME + "_GENERATOR")
    @SequenceGenerator(
            name = TABLE_NAME + "_GENERATOR",
            sequenceName = TABLE_NAME + "_SEQUENCE"
    )
    @Column(name = "FRIEND_ID")
    Long id;

    @Id
    @Column(name = "USERNAME")
    private @Getter String name;

    @Column(name = "FRIEND_USERNAME")
    private @Getter String friendName;

    @Enumerated(EnumType.ORDINAL)
    private @Getter Status status;


    public Friend(String name, String friendName, Status status) {
        this.name = name;
        this.friendName = friendName;
        this.status = status;
    }

    public Friend() {
        this.id = null;
        this.name = "";
        this.friendName = "";
        this.status = Status.INVALID;
    }



}
