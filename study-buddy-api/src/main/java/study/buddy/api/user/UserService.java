package study.buddy.api.user;


import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.buddy.api.security.PassHash;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    public PassHash hasher;

    public Map<String, Object> getUserStripped(String username) {
        return userRepository.getUserSafe(username);
    }
    public boolean hasUsername(String username) {
        return userRepository.countByName(username) > 0;
    }

    public Optional<User> findUser(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> searchUsers(String subName) {
        List<User> users = userRepository.findByNameLike("%"+subName+"%");
        //clear user info
        for(User user : users) {
            user.setPassword("");
        }
        return users;
    }
    public List<User> searchUsers(String username, String subName) {
        User u = userRepository.findByName(username).get();
        List<User> users = userRepository.findByNameLikeAndSchool("%"+subName+"%", u.getSchool());
        //clear user info
        for(User user : users) {
            user.setPassword("");
        }
        return users;
    }

    public Optional<User> findUsername(String username) { return userRepository.findByName(username);}

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User initUser(String name, String emailAddress, String password, String userType, String school){
        User u = new User(name, emailAddress, password, userType, school);
        hasher.hashPass(u, password);
        return userRepository.save(u);
    }

    public User editProfile(String username, String newProfile) {
        Optional<User> user = findUsername(username);
        if(user.isPresent()) {
            User u = user.get();
            u.setProfile(newProfile);
            return userRepository.save(u);
        }

        return null;
    }
}