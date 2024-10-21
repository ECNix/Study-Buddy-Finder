package study.buddy.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.buddy.api.session.*;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class SessionTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    public SessionService session_Service = new SessionService();

    private Session session;

    @BeforeEach
    public void initializeTestCases() {
        TimeZone tz = TimeZone.getTimeZone("GMT+9:00");
        Locale loc = new Locale("ja", "JP", "JP");
        Calendar calendar = Calendar.getInstance(loc);
        /// Does not work: GregorianCalendar gc = (GregorianCalendar) calendar;
        GregorianCalendar gc = new GregorianCalendar(tz);
        //session_Service = new SessionService();
        session = new Session("jimB0", "obama", 25, 1, 1L, "Baylor", gc, gc, false);
        Assertions.assertNotNull(session);
        //Assertions.assertNotNull(sessionRepository);
        Assertions.assertNotNull(session_Service);
    }


    @DisplayName("Test to save session")
    @Test
    public void saveSessionTest() {
        //given(sessionRepository.save(session)).willReturn(session);

        System.out.println(sessionRepository);
        System.out.println(session_Service);

        Session s = session_Service.saveSession(session);

        System.out.println(session.getName());
        System.out.println(session.getOwner());
        System.out.println(session.getStudentCapacity());
        System.out.println(session.getTutorCapacity());
        System.out.println(session.getCourse());
        System.out.println(session.getLocation());
        System.out.println(session.getSDate());
        System.out.println(session.getEDate());
    }

    @DisplayName("Test to return all sessions")
    @Test
    public void getAllSessionsTest() {
        //List<Session> sessionList = session_Service.getAllSessions();
        //Assertions.assertNotNull(sessionList);
        Assertions.assertNotNull(session_Service.getAllSessions());
        //Assertions.assertNotEquals(sessionList.size(), 0, "Get some sessions");
    }
    @DisplayName("Test to return session given id")
    @Test
    public void getSessionUsingIdTest() {
        Long l = session.getId();
        Optional<Session> sessionList = session_Service.findSessionByID(l);
        Assertions.assertNotNull(sessionList);
    }

}