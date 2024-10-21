package study.buddy.api.course;

import java.io.Serializable;

public class CourseID implements Serializable {
    private String name;
    private String school;

    public CourseID(){
        this.name = "";
        this.school = "";
    }

    public CourseID(String name, String school) {
        this.name = name;
        this.school = school;
    }
}