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
import study.buddy.api.session.*;
import study.buddy.api.study.Study;
import study.buddy.api.study.StudyID;
import study.buddy.api.study.StudyRepository;
import study.buddy.api.study.StudyService;

import java.util.List;
import java.util.Optional;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class StudyTest {
    @Mock
    //@Autowired
    private StudyRepository studyRepository;
    @InjectMocks
    //@Autowired
    public StudyService study_Service = new StudyService();
    private Study study_status;

    @BeforeEach
    public void initializeTestCases() {
        //study_Service = new StudyService();
        study_status = new Study("jimB0", 0, true);
        Assertions.assertNotNull(study_status);
        //Assertions.assertNotNull(studyRepository);
        Assertions.assertNotNull(study_Service);
    }
    /*
    @DisplayName("Test to return info of all studies")
    @Test
    public void getAllStudyesTest() {
        given(studyRepository.findAll()).willReturn(List.of(study_status));
        List<Study> studyList = study_Service.getAllStudyesInfo();
        Assertions.assertNotNull(studyList);
        Assertions.assertNotEquals(studyList.size(), 0, "Take some studyes");
    }*/
    @DisplayName("Test to find study given username and course")
    @Test
    public void findStudyUsingIdTest() {
        StudyID id = new StudyID(study_status.getUsername(), study_status.getCourseID());
        //given(studyRepository.findById(id)).willReturn(Optional.of(study_status));

        Optional<Study> c = study_Service.getSpecificStudy(study_status.getUsername(), study_status.getCourseID());
        Optional<Study> studyList = study_Service.getSpecificStudy(study_status.getUsername(), study_status.getCourseID());
        //TODO
        //Assertions.assertNotNull(studyList.get());
    }
    @DisplayName("Test to find a user's studies")
    @Test
    public void findNameFromStudyTest() {
        String s = study_status.getUsername();
        List<Study> studyList = study_Service.getUserStudies(s);
        Assertions.assertNotNull(studyList);
    }
    /*@DisplayName("Test to find study given the course")
    @Test
    public void findStudyByCourseTest() {
        Course course = study_status.getCourse();
        List<Study> studyList = study_Service.findStudyByCourse(course);
        Assertions.assertNotNull(studyList);
    }*/
}