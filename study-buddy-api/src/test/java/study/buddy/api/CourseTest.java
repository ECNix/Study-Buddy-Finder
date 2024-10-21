package study.buddy.api;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import study.buddy.api.course.*;

import java.util.List;
import java.util.Optional;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class CourseTest {
    @Mock
    //@Autowired
    private CourseRepository courseRepository;
    @InjectMocks
    //@Autowired
    public CourseService courseService = new CourseService();
    private Course course;

    @BeforeEach
    public void initializeTestCases() {
        //class_Service = new ClassService();
        course = new Course("CALCULUS I", Subject.MATH, "Baylor University");
        Assertions.assertNotNull(course);
        //Assertions.assertNotNull(courseRepository);
        Assertions.assertNotNull(courseService);
    }
    @DisplayName("Test to return info of all courses")
    @Test
    public void getAllCoursesTest() {
        given(courseRepository.findAll()).willReturn(List.of(course));
        List<Course> courseList = courseService.findAllCourses();
        Assertions.assertNotNull(courseList);
        //Assertions.assertFalse(courseList.isEmpty(), "No Courses");
    }
    @DisplayName("Test to find class given id")
    @Test
    public void findCourseUsingIdTest() {
        Long l = course.getId();
        given(courseRepository.findById(l)).willReturn(Optional.of(course));

        Course c = courseService.findByID(course.getId()).get();
        Optional<Course> courseList = courseService.findByID(l);
        Assertions.assertNotNull(courseList);
    }/*
    @DisplayName("Test to find course given name")
    @Test
    public void findCourseByNameTest() {
        String s = course.getName();
        List<Course> courseList = courseService.findName(s);
        Assertions.assertNotNull(courseList);
    }
    @DisplayName("Test to find class given subject")
    @Test
    public void findCourseBySubjectTest() {
        String s = course.getSubject();
        List<Course> courseList = courseService.findSubject(s);
        Assertions.assertNotNull(courseList);
    }*/
    @DisplayName("Test to find class given name and school")
    @Test
    public void findCourseBySchoolTest() {
        String s = course.getSchool();
        String n = course.getName();
        //Long courseID = courseService.findByNameAndSchool(n, s);
        //TODO
        //Assertions.assertNotNull(courseID);
    }
}
