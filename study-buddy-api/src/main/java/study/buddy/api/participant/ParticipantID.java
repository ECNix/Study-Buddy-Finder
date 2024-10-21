package study.buddy.api.participant;

import java.io.Serializable;

public class ParticipantID implements Serializable {
    private String username;
    private Long sessionID;

    public ParticipantID(){
        this.username = "";
        this.sessionID = null;
    }

    public ParticipantID(String name, Long sessID) {
        this.username = name;
        this.sessionID = sessID;
    }
}
