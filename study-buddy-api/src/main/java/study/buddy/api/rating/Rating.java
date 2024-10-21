package study.buddy.api.rating;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
@Table(name = Rating.TABLE_NAME)
public class Rating {
    public static final String TABLE_NAME = "TUTOR_RATING";

    @Id
    @GeneratedValue(generator = TABLE_NAME + "_GENERATOR")
    @SequenceGenerator(
            name = TABLE_NAME + "_GENERATOR",
            sequenceName = TABLE_NAME + "_SEQUENCE"
    )
    @Column(name = "USER_ID")
    Long id;

    @Column(name = "SESSION_ID")
    private @Getter Long sessionID;

    @Column(name = "TUTOR")
    private @Getter String tutorName;

    @Column(name = "RATER")
    private @Getter String raterName;

    @Column(name = "RATING")
    private @Getter Double rating;

    public Rating(Long sessionID, String tutorName, String raterName, Double rating) {
        this.sessionID = sessionID;
        this.tutorName = tutorName;
        this.raterName = raterName;
        this.rating = rating;
    }
    public Rating() {}
}
