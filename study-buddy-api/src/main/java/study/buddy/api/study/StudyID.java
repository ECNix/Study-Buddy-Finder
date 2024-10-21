package study.buddy.api.study;

import java.io.Serializable;

public class StudyID implements Serializable {
    private String username;
    private Long courseID;

    public StudyID(){
        this.username = "";
        this.courseID = null;
    }

    public StudyID(String name, Long sessID) {
        this.username = name;
        this.courseID = sessID;
    }
}
