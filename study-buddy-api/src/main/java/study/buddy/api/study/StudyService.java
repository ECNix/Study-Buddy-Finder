package study.buddy.api.study;


import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyService {
    //@Autowired
    //private UserService userService;
    @Autowired
    private StudyRepository studyRepository;

    public Optional<Study> getSpecificStudy(String username, Long courseID) { return studyRepository.findByUsernameAndCourseID(username, courseID); }
    public List<Study> getUserStudies(String username) {
        return studyRepository.findByUsername(username);
    }

    public Study saveStudy(Study s){ return studyRepository.save(s); }

    public Long cancelStudy(String username, Long courseID) { return studyRepository.removeByUsernameAndCourseID(username, courseID); }


    /*
    @Transactional
    public Long deleteClass(String username, Course course) {
        return studyRepository.removeByUsernameAndCourse(username, course);
    }

    public Study createClass(String username, Course course, Study.Status status) {
        List<User> users = userService.findName(username);
        User user;
        if(users.size() > 0) {
            user = users.get(0);
        }
        else {
            return null;
        }
        if(status == Study.Status.TEACHES && !user.getUserType().equals("tutor")) {
            return null;
        }
        List<Study> matches = studyRepository.findByUsernameAndCourse(username, course);
        Study c = new Study(username, course, status);
        if(matches.size() > 0) {
            Study prev = matches.get(0);
            if(prev.getStatus() != status) {
                c = new Study(username, course, Study.Status.ENROLLED_AND_TEACHES);
            }
        }
        return studyRepository.save(c);
    }

    public List<Study> getAllClassesInfo(){
        List<Study> debug = studyRepository.findAll();
        for(Study c : debug){
            System.out.println(c.username);
            System.out.println(c.course);
            System.out.println(c.status);
        }
        return debug;
    }
    public List<Course> getAllCourses() {
        return Arrays.asList(Course.class.getEnumConstants());
    }*/

}