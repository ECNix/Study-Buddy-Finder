package study.buddy.api.course;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.buddy.api.security.JWToken;
import study.buddy.api.study.StudyRepository;


import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;


    public List<Course> findAllCourses() { return courseRepository.findAll(); }
    public Long findByNameAndSchool(String name, String school) { return courseRepository.findByNameAndSchool(name, school).get(0).getId(); }
    public Optional<Course> findByID(Long courseId) {
        return courseRepository.findById(courseId);
    }
    public List<Course> findBySchool(String school){ return courseRepository.findBySchool(school); }
    public Subject[] getSubjects(){ return Subject.values(); }


    public Course insertCourse(Course course) { return courseRepository.save(course); }


}
