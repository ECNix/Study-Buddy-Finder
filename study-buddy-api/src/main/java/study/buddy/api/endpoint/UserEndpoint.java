package study.buddy.api.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import study.buddy.api.security.JWToken;
import study.buddy.api.user.User;
import study.buddy.api.user.UserService;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import study.buddy.api.security.PassHash;

@Log4j2
@RestController
public class UserEndpoint {
    @Autowired
    private PassHash hasher;

    @Autowired
    private UserService userService;

    JWToken jwToken = new JWToken();


    @GetMapping("/user/find")
    public List<User> findUser(@RequestHeader String Authorization, @RequestParam String subName) {
        String sender = jwToken.getSubject(Authorization);
        return userService.searchUsers(sender, subName);
    }

//    @GetMapping("/user/{id}")
//    public User findUserById(@PathVariable Long id) {
//        var user = userService.findUser(id);
//        return user.orElse(null);
//    }


//    @GetMapping("/userName")
//    public User findUserByName(@RequestParam String stuName) {
//        Optional<User> egg;
//        egg = userService.findUsername(stuName);
//        User user = null;
//        if(egg.isPresent())
//        {   user = egg.get(); }
//
//        return user;
//    }

    @GetMapping("/user/view")
    public Map<String, Object> findUserProfile(@RequestParam String stuName) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", "");
        result.put("emailAddress", "");
        result.put("profile", "");
        result.put("school", "");
        result.put("userType", "");
        result.put("status", false);
        Optional<User> user = userService.findUsername(stuName);

        if(user.isPresent()) {
            result.put("name", user.get().getName());
            result.put("emailAddress", user.get().getEmailAddress());
            result.put("profile", user.get().getProfile());
            result.put("school", user.get().getSchool());
            result.put("userType", user.get().getUserType());
            result.put("status", true);
        }
        return result;
    }

    @GetMapping("/user/login")
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> returnPacket = new HashMap<>();
        returnPacket.put("success", false);
        returnPacket.put("token", -1);
        if(!userService.hasUsername(username)) {
            return returnPacket;
        }
        User user = userService.findUsername(username).get();
        boolean isLogin = hasher.validation(user, password);
        if(isLogin) {
            returnPacket.put("success", true);
            returnPacket.put("token", 1);
            returnPacket.put("name", user.getName());
            returnPacket.put("usertype", user.getUserType());
            returnPacket.put("school", user.getSchool());
            String token = jwToken.createToken(user.getName());
            returnPacket.put("token", token);

        }
        return returnPacket;
    }

    @PostMapping("/user")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/user/profile")
    public User saveUser(@RequestBody Map<String, String> profile) {
        String username = profile.get("username");
        String profileInfo = profile.get("profile");
        User u = userService.editProfile(username, profileInfo);
        return u;
    }

    @PostMapping ("/user/register")
    public Map<String, Object> initUser(@RequestBody User regUser) {
        Optional<User> u = userService.findUsername(regUser.getName());

        Map<String, Object> returnPacket = new HashMap<>();

        returnPacket.put("success", true);
        if(u.isPresent()){
            returnPacket.put("success", false);
            return returnPacket;
        }
        userService.initUser(
                regUser.getName(),
                regUser.getEmailAddress(),
                regUser.getPassword(),
                regUser.getUserType(),
                regUser.getSchool()
        );
        return returnPacket;
    }

    @GetMapping("/user/refresh")
    public Map<String, String> refreshToken(@RequestHeader String Authorization) {
        Map<String, String> result = new HashMap<>();
        String username = jwToken.getSubject(Authorization);
        String token = jwToken.createToken(username);
        result.put("token", token);
        return result;
    }
}
