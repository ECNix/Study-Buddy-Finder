package study.buddy.api.endpoint;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import kotlin.Pair;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import study.buddy.api.course.Course;
import study.buddy.api.course.CourseService;
import study.buddy.api.friend.Friend;
import study.buddy.api.friend.FriendService;
import study.buddy.api.notification.Notification;
import study.buddy.api.notification.NotificationService;
import study.buddy.api.participant.Participant;
import study.buddy.api.participant.ParticipantService;
import study.buddy.api.security.JWToken;
import study.buddy.api.session.Session;
import study.buddy.api.session.SessionService;
import study.buddy.api.study.Study;
import study.buddy.api.study.StudyService;
import study.buddy.api.user.User;
import study.buddy.api.user.UserService;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Log4j2
@RestController
@EnableScheduling
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SessionEndpoint {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private StudyService studyService;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    JWToken jwToken;

    @GetMapping("/sessions/specific")
    public Map<String, Object> findSessionById(@RequestParam Long id) {
        Optional<Session> session = sessionService.findSessionByID(id);
        if(session.isEmpty())
            return new HashMap<>();
        return session.get().toMap();
    }
    @GetMapping("/sessions/mine")
    public List<Map<String, Object>> getUserSessions(@RequestHeader String Authorization){
        String owner = jwToken.getSubject(Authorization);
        List<Session> sessions = sessionService.getUserSessions(owner);
        List<Map<String, Object>> ret = new ArrayList<>();
        for(Session s: sessions){
            ret.add(s.toMap());
        }
        return ret;
    }

    @GetMapping("/sessions/upcoming")
    public List<Map<String, Object>> getUpcomingSessions(@RequestHeader String Authorization){
        String username = jwToken.getSubject(Authorization);
        List<Map<String, Object>> sessions = new ArrayList<>();
        List<Participant> participations = participantService.getParticipating(username);
        //FIXME: Make more efficient with query?
        for(Participant p: participations){
            Session s = sessionService.findSessionByID(p.getSessionID()).get();
            if(!s.getOwner().equals(username))
                sessions.add(s.toMap());
        }
        return sessions;
    }
    @GetMapping("/sessions/priorities")
    public List<Map<String, Object>> getSessionsWithPriorities(@RequestHeader String Authorization){
        String username = jwToken.getSubject(Authorization);
        List<Session> sessions = sessionService.getAllSessions();
        List<Pair<Session, Integer>> ordered = new ArrayList<>();
        Optional<Friend> friendRequest;
        for(Session s: sessions){
            int priority = 1;
            Course c = courseService.findByID(s.getCourse()).get();
            User u = userService.findUsername(username).get();
            if(!u.getSchool().equals(c.getSchool()) || s.getOwner().equals(username))
                priority = 0;
            else if(s.getIsPrivate() &&
                    ((friendRequest=friendService.findRequest(username, s.getOwner())).isEmpty() ||
                            friendRequest.get().getStatus() != Friend.Status.ACCEPTED)){
                priority = 0;
            }
            else {
                //FIXME: There's gotta be a way to make this more efficient...
                int numParticipants = 0;
                //Determine if the session has any of the user's friends and count total participants
                for(Participant part: participantService.getParticipantsInSession(s.getId())){
                    for(Friend friend: friendService.findFriends(username)){
                        if(part.getUsername().equals(friend.getFriendName())){
                            priority *= 2;
                            break;
                        }
                    }
                    numParticipants++;
                }
                //Determine if the session's course is one the user is studying and there is capacity for them
                int numStudents = participantService.getStudentsInSession(s.getId()).size();
                boolean matchFound = false;
                for(Study study : studyService.getUserStudies(username)) {
                    if(study.getCourseID().equals(s.getCourse())){
                        if(study.isTutor() && s.getTutorCapacity() > 0 && s.getTutorCapacity() > (numParticipants - numStudents))
                            priority *= 3;
                        else if(!study.isTutor() && s.getStudentCapacity() > numStudents)
                            priority *= 3;
                        else
                            priority = 0;
                        matchFound = true;
                        break;
                    }
                }
                //If we haven't checked for capacity, check for capacity
                if(!matchFound && s.getStudentCapacity() <= numStudents)
                    priority = 0;
            }
            if(priority != 0)
                ordered.add(new Pair<>(s, priority));
        }
        //Return the correct structure, ordered by priority
        List<Map<String, Object>> ret = new ArrayList<>();
        ordered.sort(Comparator.comparing(p -> -p.getSecond()));
        for(Pair<Session, Integer> s: ordered){
            ret.add(s.getFirst().toMap());
        }
        return ret;
    }
    @Scheduled(fixedRate = 60000)
    @GetMapping("/sessions/notify")
    public void sendUpcomingNotifications(){
        List<Session> sessions = sessionService.getAllSessions();
        for(Session s: sessions){
            GregorianCalendar future = new GregorianCalendar();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddhhmm");
            future.add(Calendar.MINUTE, 15);
            future.get(Calendar.HOUR_OF_DAY);
            if(fmt.format(s.getSDate().getTime()).equals(fmt.format(future.getTime()))){
                for(Participant p: participantService.getParticipantsInSession(s.getId())){
                    notificationService.saveNotification(
                            new Notification(p.getUsername(), null, s.getId(), Notification.NotifType.UPCOMING_SESSION,
                                    s.getName() + " is starting soon")
                    );
                }
            }
        }
    }

    @GetMapping("/sessions")
    public List<Map<String, Object>> findAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        List<Map<String, Object>> ret = new ArrayList<>();
        for(Session s: sessions){
            ret.add(s.toMap());
        }
        return ret;
    }

    @Transactional
    @PostMapping("/session/remove")
    public Long removeSession(@RequestHeader String Authorization, @RequestBody Map<String, String> sessionVals){
        String username = jwToken.getSubject(Authorization);
        Optional<Session> s = sessionService.findSessionByID(Long.parseLong(sessionVals.get("id")));
        if(s.isPresent() && username.equals(s.get().getOwner())) {
            sessionService.removeSession(s.get().getId());
            List<Notification> notifs = notificationService.findBySessionID(s.get().getId());
            for(Notification n: notifs){
                notificationService.deleteById(n.getId());
            }
            return 1L;
        }
        return 0L;
    }
    @Transactional
    @Scheduled(fixedRate = 86400000)
    @PostMapping("/sessions/old")
    public void removeOldSessions(){
        for(Session s: sessionService.getAllSessions()){
            if(s.getEDate().before(new GregorianCalendar())) {
                participantService.removeSessionParticipants(s.getId());
                sessionService.removeSession(s.getId());
            }
        }
    }
    @PostMapping("/session/create")
    public Long saveSession(@RequestHeader String Authorization, @RequestBody Map<String, String> sessionVals) {
        //create date values
        GregorianCalendar s = new GregorianCalendar();
        GregorianCalendar e = new GregorianCalendar();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            s.setTime(df.parse(sessionVals.get("start")));
            e.setTime(df.parse(sessionVals.get("end")));
        }
        catch(ParseException err){
            return -1L;
        }
        //Create session using values
        Session sess = new Session(
            sessionVals.get("name"),
            jwToken.getSubject(Authorization),
            Integer.parseInt(sessionVals.get("sCap")),
            Integer.parseInt(sessionVals.get("tCap")),
            Long.parseLong(sessionVals.get("course")),
            sessionVals.get("loc"),
            s,
            e,
            Boolean.parseBoolean(sessionVals.get("private"))
        );
        //Handle if session needs updating instead of insertion
        if(!sessionVals.get("id").equals("-1"))
            sess.setId(Long.parseLong(sessionVals.get("id")));
        //Save sessions
        return sessionService.saveSession(sess).getId();
    }
}
