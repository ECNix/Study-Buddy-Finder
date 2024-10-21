package study.buddy.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.buddy.api.security.PassHash;
import study.buddy.api.user.*;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    //@Autowired
    private UserRepository userRepository;

    @InjectMocks
    //@Autowired
    public UserService user_Service = new UserService();

    private User user;

    @BeforeEach
    public void initializeTestCases() {
        //user_Service = new UserService();
        user = new User("jimB0", "jimbo@boi", "password", "student", "Baylor University");
        user.setId(1L);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user_Service);
    }


    @DisplayName("Test to save user")
    @Test
    public void saveUserTest() {
        given(userRepository.save(user)).willReturn(user);

        System.out.println(userRepository);
        System.out.println(user_Service);

        User u = user_Service.saveUser(user);

        System.out.println(u.getName());
        System.out.println(u.getEmailAddress());
        System.out.println(u.getPassword());
        System.out.println(u.getUserType());

        Assertions.assertNotNull(u);
    }

    @DisplayName("Test to find user given username")
    @Test
    public void findUserNameTest() {
        String s = user.getName();
        //List<User> userList = user_Service.searchUsers(s);

        //given(userRepository.findByName(s)).willReturn(Optional.of(user));

        //Assertions.assertNotNull(userList);
        Assertions.assertNotNull(user_Service.searchUsers(s));
        //Assertions.assertFalse(userList.isEmpty());
    }
    @DisplayName("Test to find user given id")
    @Test
    public void findUserIdTest() {
        Long l = user.getId();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        User u = user_Service.findUser(user.getId()).get();
        Optional<User> userList = user_Service.findUser(l);
        Assertions.assertNotNull(u);
        //Assertions.assertFalse(userList.isEmpty());
    }

    @DisplayName("Test to search user")
    @Test
    public void searchUsersTest() {
        String s = "jim";
        List<User> userList = user_Service.searchUsers(s);
        Assertions.assertNotNull(userList);
        //Assertions.assertFalse(userList.isEmpty());
    }
    @DisplayName("Test to register user")
    @Test
    public void initUserTest() {
        //given(userRepository.save(user)).willReturn(user);
        user_Service.hasher = new PassHash();

        User u = user_Service.initUser(user.getName(), user.getEmailAddress(), user.getPassword(), user.getUserType(), user.getSchool());
        //Assertions.assertNotNull(u);

        verify(userRepository, never()).save(u);
    }
}