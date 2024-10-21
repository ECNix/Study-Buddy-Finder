package study.buddy.api.session;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = Session.TABLE_NAME)
public class Session {
    public static final String TABLE_NAME = "SESSION";

    @Id
    @GeneratedValue(generator = TABLE_NAME + "_GENERATOR")
    @SequenceGenerator(
            name = TABLE_NAME + "_GENERATOR",
            sequenceName = TABLE_NAME + "_SEQUENCE"
    )
    @Column(name = "SESSION_ID")
    @Getter
    Long id;

    @Column(name = "NAME")
    @Getter
    String name;

    @Column(name = "OWNER")
    @Getter
    String owner;

    @Column(name = "STUDENT_CAPACITY")
    @Getter
    Integer studentCapacity;

    @Column(name = "TUTOR_CAPACITY")
    @Getter
    Integer tutorCapacity;

    @Column(name = "COURSE")
    @Getter
    Long course;

    @Column(name = "LOCATION")
    @Getter
    String location;

    @Column(name = "START_DATE")
    @Getter
    GregorianCalendar sDate;

    @Column(name = "END_DATE")
    @Getter
    GregorianCalendar eDate;

    @Column(name = "IS_PRIVATE")
    @Getter
    Boolean isPrivate;

    public Session(String n, String o, int sCap, int tCap, long c, String l, GregorianCalendar s, GregorianCalendar e, boolean p){
        name = n;
        owner = o;
        studentCapacity = sCap;
        tutorCapacity = tCap;
        course = c;
        location = l;
        sDate = s;
        eDate = e;
        isPrivate = p;
    }
    public Session(){ }
    
    public Map<String, Object> toMap(){
        Map<String, Object> ret = new HashMap<>();
        ret.put("id", id);
        ret.put("name", name);
        ret.put("day", new SimpleDateFormat("dd/MM/yyyy").format(sDate.getTime()));
        ret.put("sdate", new SimpleDateFormat("HH:mm").format(sDate.getTime()));
        ret.put("edate", new SimpleDateFormat("HH:mm").format(eDate.getTime()));
        ret.put("studentCapacity", studentCapacity);
        ret.put("tutorCapacity", tutorCapacity);
        ret.put("location", location);
        ret.put("course", course.toString());
        ret.put("owner", owner);
        ret.put("private", isPrivate);
        return ret;
    }
}