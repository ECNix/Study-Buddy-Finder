package study.buddy.api.study;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
@IdClass(StudyID.class)
@Table(name = Study.TABLE_NAME)
public class Study {
    public static final String TABLE_NAME = "STUDY";

    @Id
    @Column(name = "USERNAME")
    @Getter String username;

    @Id
    @Column(name = "COURSE")
    @Getter Long courseID;

    @Column(name = "TUTOR_STATUS")
    @Getter boolean isTutor;

    public Study(String username, long course, boolean isTutor) {
        this.username = username;
        this.courseID = course;
        this.isTutor = isTutor;
    }

    public Study() {
        this.username = "";
        this.courseID = null;
        this.isTutor = false;
    }

}