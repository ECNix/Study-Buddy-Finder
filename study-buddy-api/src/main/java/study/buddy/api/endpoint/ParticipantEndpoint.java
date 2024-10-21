package study.buddy.api.endpoint;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import study.buddy.api.participant.Participant;
import study.buddy.api.participant.ParticipantService;
import study.buddy.api.security.JWToken;
import study.buddy.api.session.Session;
import study.buddy.api.session.SessionService;
import study.buddy.api.study.Study;
import study.buddy.api.study.StudyService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RestController
public class ParticipantEndpoint {
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private StudyService studyService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    JWToken jwToken;

    @GetMapping("/participants/students")
    public List<Participant> getStudentsInSession(@RequestParam Long id) {
        return participantService.getStudentsInSession(id);
    }
    @GetMapping("/participants/tutors")
    public List<Participant> getTutorsInSession(@RequestParam Long id) {
        return participantService.getTutorsInSession(id);
    }
    @GetMapping("/participants/session")
    public List<Participant> getParticipantsInSession(@RequestParam Long id) {
        return participantService.getParticipantsInSession(id);
    }

    @GetMapping("/participants/exists")
    public boolean participantInSession(@RequestHeader String Authorization, @RequestParam Long id){
        String username = jwToken.getSubject(Authorization);
        return participantService.getParticipant(username, id).isPresent();
    }
    @GetMapping("/participants/participating")
    public List<Participant> participatingIn(@RequestHeader String Authorization){
        String username = jwToken.getSubject(Authorization);
        return participantService.getParticipating(username);
    }

    @PostMapping("/participants/add")
    public Long addParticipant(@RequestHeader String Authorization, @RequestBody Map<String, String> partVals){
        String username = jwToken.getSubject(Authorization);

        Long c = sessionService.findSessionByID(Long.parseLong(partVals.get("session"))).get().getCourse();
        boolean tutor = false;
        Optional<Study> study = studyService.getSpecificStudy(username, c);
        if(study.isPresent() && study.get().isTutor())
            tutor = true;

        Participant p = participantService.addParticipant(
            new Participant(
                username,
                Long.parseLong(partVals.get("session")),
                tutor
            )
        );
        if(p == null)
            return 0L;
        else
            return 1L;
    }
    @Transactional
    @PostMapping("/participants/remove")
    public Long removeParticipant(@RequestHeader String Authorization, @RequestBody Map<String, String> partVals){
        String username = jwToken.getSubject(Authorization);
        return participantService.removeParticipant(username, Long.parseLong(partVals.get("id")));
    }
    @Transactional
    @PostMapping("/participants/remove/session")
    public Long removeParticipants(@RequestBody Map<String, String> partVals){
        return participantService.removeSessionParticipants(Long.parseLong(partVals.get("sessID")));
    }
}
