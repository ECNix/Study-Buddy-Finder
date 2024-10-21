package study.buddy.api.user;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;

@Data
@Entity
@IdClass(UserID.class)
@Table(name = User.TABLE_NAME)
public class User{
    public static final String TABLE_NAME = "USER";

    @Id
    @GeneratedValue(generator = TABLE_NAME + "_GENERATOR")
    @SequenceGenerator(
            name = TABLE_NAME + "_GENERATOR",
            sequenceName = TABLE_NAME + "_SEQUENCE"
    )
    @Column(name = "USER_ID")

    Long id;

    @Id
    @Column(name = "USERNAME", unique = true, nullable = false)
    private @Getter String name;

    @Column(name = "EMAIL_ADDRESS", unique = true, nullable = false)
    private @Getter String emailAddress;

    @Column(name = "PROFILE_INFO")
    private @Getter String profile;

    @Column(name = "PASSWORD")
    private @Getter String password;

    @Column(name = "SCHOOL")
    private @Getter String school;

    @Column(name = "USER_TYPE")
    private @Getter String userType;



    public User(String n, String e, String p, String s){
        //id = i;

        name = n;
        emailAddress = e;
        password = p;
        userType = "student";
        school = s;
        profile = "";
    }
    public User(String n, String e, String p, String t, String s){
        //id = i;

        name = n;
        emailAddress = e;
        password = p;
        userType = t;
        profile = "";
        school = s;
    }

    public User() {
        name = "INVALID";
        id = null;
        emailAddress = "";
        password = "password";
        userType = "student";
        school = "";
        profile = "";
    }

}


