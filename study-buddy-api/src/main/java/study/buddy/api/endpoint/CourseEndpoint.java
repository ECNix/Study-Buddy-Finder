package study.buddy.api.endpoint;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import study.buddy.api.course.*;
import study.buddy.api.security.JWToken;
import study.buddy.api.user.User;
import study.buddy.api.user.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RestController
public class CourseEndpoint {
    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    JWToken jwToken;

    @PostMapping("/courses/add")
    public int addCourse(@RequestHeader String Authorization, @RequestBody Map<String, String> courseVals){
        Optional<User> u = userService.findUsername(jwToken.getSubject(Authorization));
        if(u.isPresent()){
            Course c = courseService.insertCourse(
                    new Course(courseVals.get("name"), Subject.valueOf(courseVals.get("subject")), u.get().getSchool())
            );
            if(c != null)
                return 0;
            else
                return 1;
        }
        else
            return 1;
    }

    @GetMapping("/courses/id")
    public Optional<Course> findCourseById(@RequestParam Long id){
        return courseService.findByID(id);
    }
    @GetMapping("/courses")
    public List<Course> allCourses() {
        return courseService.findAllCourses();
    }
    @GetMapping("/courses/values")
    public Long findCourseByNameAndSchool(@RequestHeader String Authorization, @RequestParam String name){
        String username = jwToken.getSubject(Authorization);
        Optional<User> user = userService.findUsername(username);
        if(user.isPresent()) {
            return courseService.findByNameAndSchool(name, user.get().getSchool());
        }
        return -1L;
    }
    @GetMapping("/courses/school")
    public List<Course> findCourseBySchool(@RequestParam String school){
        return courseService.findBySchool(school);
    }

    @GetMapping("/subjects")
    public Subject[] getSubjects(){ return  courseService.getSubjects(); }
}
