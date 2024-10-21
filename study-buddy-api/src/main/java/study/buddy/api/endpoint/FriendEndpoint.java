package study.buddy.api.endpoint;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import study.buddy.api.friend.Friend;
import study.buddy.api.friend.FriendService;

import org.springframework.web.bind.annotation.*;
import study.buddy.api.security.JWToken;
import study.buddy.api.study.Study;
import study.buddy.api.study.StudyService;
import study.buddy.api.user.User;
import study.buddy.api.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RestController
public class FriendEndpoint {
    @Autowired
    private FriendService friendService;
    @Autowired
    private StudyService studyService;
    @Autowired
    private UserService userService;
    @Autowired
    JWToken jwToken;


    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/friend/all")
    public List<Friend> allFriends() {
        return friendService.findAllFriends();
    }

    //@CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/friend/request")
    public Boolean createFriendRequest(@RequestHeader String Authorization, @RequestBody Map<String, String> data) {
        String sender = jwToken.getSubject(Authorization);
        Friend friend = friendService.sendRequest(sender, data.get("friendName"));
        return (friend != null);
    }

    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/friend/friends")
    public List<Friend> getFriends(@RequestHeader String Authorization) {
        String sender = jwToken.getSubject(Authorization);

        return friendService.findFriends(sender);
    }
    @GetMapping("/friend/accepted")
    public List<Friend> getAcceptedFriends(@RequestHeader String Authorization) {
        String sender = jwToken.getSubject(Authorization);
        return friendService.findFriendsAccepted(sender);
    }
    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/friend/requests")
    public List<Friend> getRequests(@RequestHeader String Authorization) {
        String sender = jwToken.getSubject(Authorization);
        return friendService.findRequests(sender);
    }

    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/friend/reject")
    public Friend rejectRequest(@RequestHeader String Authorization, @RequestParam String sender) {
        String friendName = jwToken.getSubject(Authorization);
        return friendService.acceptRequest(sender, friendName, Friend.Status.DENIED);
    }

    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/friend/accept")
    public Friend acceptRequest(@RequestHeader String Authorization, @RequestParam String sender) {
        String friendName = jwToken.getSubject(Authorization);
        return friendService.acceptRequest(sender, friendName, Friend.Status.ACCEPTED);
    }

    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/friend/delete")
    public Long deleteFriend(@RequestHeader String Authorization, @RequestParam String friendName) {
        String sender = jwToken.getSubject(Authorization);
        return friendService.deleteFriend(sender, friendName);
    }

    @GetMapping("/friend/recommendation")
    public List<User> friendReccs(@RequestHeader String Authorization){
        /*TODO: Return a list of potential friend requests for other
           users who take the same classes as the current user.
           Input name will be the name of the current user*/
        //temporary output, delete later
        String sender = jwToken.getSubject(Authorization);
        return friendService.findRecommendations(sender);
    }

    @GetMapping("/friend/tutors")
    public List<Map<String, Object>> tutorRecs(@RequestHeader String Authorization, @RequestParam Long course){
        String sender = jwToken.getSubject(Authorization);

        List<Friend> friends = friendService.findFriends(sender);
        System.out.println(sender + " has " + friends.size() + " friends");
        List<Map<String, Object>> tutors = new ArrayList<>();
        for(Friend f: friends){
            System.out.println("FRIEND: " + f.getFriendName());
            Optional<Study> s = studyService.getSpecificStudy(f.getFriendName(), course);
            if(s.isPresent() && s.get().isTutor())
                tutors.add(userService.getUserStripped(f.getFriendName()));
        }
        return tutors;
    }
    @GetMapping("/friend/buddytutors")
    public List<User> tutorBuddyRecs(@RequestHeader String Authorization){
        String sender = jwToken.getSubject(Authorization);

        return friendService.findTutorRecommendations(sender);
    }
}
