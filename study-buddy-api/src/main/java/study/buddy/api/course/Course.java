package study.buddy.api.course;


import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
@Table(name = Course.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "SCHOOL" }) })
public class Course {
    public static final String TABLE_NAME = "COURSE";

    @Id
    @GeneratedValue(generator = TABLE_NAME + "_GENERATOR")
    @SequenceGenerator(
            name = TABLE_NAME + "_GENERATOR",
            sequenceName = TABLE_NAME + "_SEQUENCE"
    )
    @Column(name = "COURSE_ID")
    @Getter Long id;


    @Column(name = "NAME")
    @Getter String name;

    @Column(name = "SUBJECT")
    @Getter Subject subject;

    @Column(name = "SCHOOL")
    @Getter String school;

    public Course(String name, Subject subject, String school) {
        this.name = name;
        this.subject = subject;
        this.school = school;
    }

    public Course() {
        this.name = "";
        this.subject = Subject.COMPUTER_SCIENCE;
        this.school = "";
    }

}