package study.buddy.api.endpoint;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import study.buddy.api.notification.Notification;
import study.buddy.api.notification.NotificationService;
import study.buddy.api.participant.Participant;
import study.buddy.api.participant.ParticipantService;
import study.buddy.api.rating.Rating;
import study.buddy.api.rating.RatingService;
import study.buddy.api.security.JWToken;
import study.buddy.api.session.Session;
import study.buddy.api.session.SessionService;

import javax.validation.constraints.Null;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Log4j2
@EnableScheduling
@RestController
public class RatingEndpoint {
    @Autowired
    RatingService ratingService;
    @Autowired
    SessionService sessionService;
    @Autowired
    ParticipantService participantService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    JWToken jwToken;

    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/tutor/rating")
    public Double getRating(@RequestParam String tutor) {
        Double rating = ratingService.getAvgRating(tutor);
        if(rating == null) {
            return 0.0;
        }
        return rating;
    }

    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/tutor/all")
    public List<Rating> getRating() {
        return ratingService.getAllRatings();
    }

    //@CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/tutor/rate")
    public Rating rate(@RequestHeader String Authorization, @RequestBody Rating rating) {

        String username = jwToken.getSubject(Authorization);
        Long sessionId = rating.getSessionID();
        Optional<Participant> p = participantService.getParticipant(username, sessionId);
        Rating r = new Rating();
        if (p.isPresent() && rating.getRaterName().equals(username)) {r = ratingService.makeRating(rating);}
        return r;
    }

    @Scheduled(fixedRate = 60000)
    @GetMapping("/rating/notify")
    public void sendTutorRatings(){
        List<Session> allSessions = sessionService.getAllSessions();
        for(Session session : allSessions) {
            GregorianCalendar future = new GregorianCalendar();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddhhmm");
//            future.add(Calendar.HOUR_OF_DAY, -6);
            future.get(Calendar.HOUR_OF_DAY);
            if(fmt.format(session.getEDate().getTime()).equals(fmt.format(future.getTime()))) {
                long id = session.getId();
                List<Participant> tutors = participantService.getTutorsInSession(id);
                List<Participant> students = participantService.getStudentsInSession(id);

                for(Participant tutor : tutors) {
                    for(Participant student : students) {
                        notificationService.saveNotification(
                                new Notification(student.getUsername(), tutor.getUsername(), session.getId(), Notification.NotifType.RATE_TUTOR,
                                        tutor.getUsername() + " needs to be rated")
                        );
                    }
                }
            }
        }
    }
}
