package study.buddy.api.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.log4j.Log4j2;
import study.buddy.api.security.JWToken;
import study.buddy.api.study.Study;
import study.buddy.api.study.StudyService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Log4j2
@RestController
public class StudyEndpoint {
    @Autowired
    StudyService studyService;
    @Autowired
    JWToken jwToken;

    @GetMapping("/study/find")
    public Optional<Study> specificStudy(@RequestHeader String Authorization, @RequestParam Long courseID) {
        String username = jwToken.getSubject(Authorization);
        return studyService.getSpecificStudy(username, courseID);
    }
    @GetMapping("/study/user")
    public List<Study> getUserStudies(@RequestHeader String Authorization) {
        String username = jwToken.getSubject(Authorization);
        return studyService.getUserStudies(username);
    }

    @PostMapping("/study/add")
    public Study enroll(@RequestHeader String Authorization, @RequestBody Map<String, String> enroll) {
        String username = jwToken.getSubject(Authorization);
        long course = Long.parseLong(enroll.get("course"));
        boolean isTutor = Boolean.parseBoolean(enroll.get("teaching"));
        return studyService.saveStudy(new Study(username, course, isTutor));
    }
    @Transactional
    @PostMapping("/study/remove")
    public Long removeClass(@RequestHeader String Authorization, @RequestBody Map<String, String> remove) {
        String username = jwToken.getSubject(Authorization);
        return studyService.cancelStudy(username, Long.parseLong(remove.get("course")));
    }


}
