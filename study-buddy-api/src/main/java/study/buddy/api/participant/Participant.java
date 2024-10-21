package study.buddy.api.participant;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;

@Data
@Entity
@IdClass(ParticipantID.class)
@Table(name = Participant.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = { "USERNAME", "SESSION_ID" }) })
public class Participant {
    public static final String TABLE_NAME = "PARTICIPANT";

    @Id
    @Column(name = "USERNAME")
    @Getter String username;

    @Id
    @Column(name = "SESSION_ID")
    @Getter Long sessionID;

    @Column(name = "IS_TUTOR")
    @Getter boolean isTutor;

    public Participant(String n, Long id, boolean t) {
        this.username = n;
        this.sessionID = id;
        this.isTutor = t;
    }

    public Participant() {
        this.username = "";
        this.sessionID = null;
        this.isTutor = false;
    }
}
