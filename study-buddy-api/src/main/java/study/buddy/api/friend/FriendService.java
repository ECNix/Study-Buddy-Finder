package study.buddy.api.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.buddy.api.study.*;
import study.buddy.api.user.User;
import study.buddy.api.user.UserRepository;

import java.util.*;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private StudyRepository studyRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Friend> findAllFriends() {return friendRepository.findAll();}

    public List<Friend> findFriends(String name) { return friendRepository.findByName(name);}
    public List<Friend> findFriendsAccepted(String name) { return friendRepository.findByNameAndStatus(name, Friend.Status.ACCEPTED);}
    public List<Friend> findRequests(String friendName) { return friendRepository.findByFriendNameAndStatus(friendName, Friend.Status.SENT);}

    public Optional<Friend> findRequest(String username, String friendName) {
        return friendRepository.findByNameAndFriendName(username, friendName);
    }

    @Transactional
    public long deleteFriend(String name, String friendName) {
        long d0 = friendRepository.deleteByNameAndFriendName(name, friendName);
        long d1 = friendRepository.deleteByNameAndFriendName(friendName, name);

        return d0 + d1;
    }

    public Friend sendRequest(String name, String friendName) {
        System.out.println(name + " " + friendName);
        if(name.equals(friendName)) {
            return null;
        }
        Optional<Friend> friend = friendRepository.findByNameAndFriendName(name, friendName);
        if(friend.isEmpty()) {
            Friend request = new Friend(name, friendName, Friend.Status.SENT);
            return friendRepository.save(request);
        }
        return null;
    }

    public Friend acceptRequest(String name, String friendName, Friend.Status status) {
        Optional<Friend> friends = friendRepository.findByNameAndFriendName(name, friendName);
        Friend friend = friends.get();
        friend.setStatus(status);

        Friend friendInverse = new Friend(friendName, name, status);
        friendRepository.save(friendInverse);
        return friendRepository.save(friend);
    }

    public List<User> findTutorRecommendations(String name){
        List<User> reccs = new ArrayList<>();
        List<Study> studyList = studyRepository.findByUsername(name);
        List<Study> potentList = new ArrayList<>();
        for(int i = 0; i < studyList.size(); i++){
            List<Study> tempList = studyRepository.findByCourseID(studyList.get(i).getCourseID());
            potentList.addAll(tempList);
        }

        Set<String> nameList = new HashSet<>();
        Map<String, Integer> nameWeight = new HashMap<>();
        for(int i = 0; i < potentList.size(); i++){
            if(potentList.get(i).isTutor()) {
                nameList.add(potentList.get(i).getUsername());
            }
            /*
            if(!nameWeight.containsKey(potentList.get(i).getUsername())){
                //if name doesnt exist yet, weight of 2 because
                //at least 1 shared class
                nameWeight.put(potentList.get(i).getUsername(), 2);
            }
            else{
                int newWeight = nameWeight.get(potentList.get(i).getUsername()) + 2;
                nameWeight.replace(potentList.get(i).getUsername(), newWeight);
            }*/

        }
        nameList.remove(name);
        //remove duplicates by feeding into a Set
        String[] nameArr = nameList.toArray(new String[0]);
        for(int i = 0; i < nameArr.length; i++){
            reccs.add(userRepository.findByName(nameArr[i]).get());
        }/*
        //sort reccs by map values
        for(int i = 0; i < nameArr.length; i++) {
            for(int j = nameArr.length-1; j > i; j--){
                if(nameWeight.get(reccs.get(j).getName()) > nameWeight.get(reccs.get(i).getName())){
                    User u = reccs.get(i);
                    reccs.remove(i);
                    reccs.add(i, reccs.get(j-1));
                    reccs.remove(j);
                    reccs.add(j, u);
                }

            }

        }*/

        return reccs;
    }

    public List<User> findRecommendations(String name){
        List<User> reccs = new ArrayList<>();
        /*TODO: Create logic to implement a weighted priority list
            same class: + 2
            maybe later - shared friend: +1

        *  */



        List<Study> studyList = studyRepository.findByUsername(name);
        List<Study> potentList = new ArrayList<>();
        for(int i = 0; i < studyList.size(); i++){
            List<Study> tempList = studyRepository.findByCourseID(studyList.get(i).getCourseID());
            potentList.addAll(tempList);
        }

        Set<String> nameList = new HashSet<>();
        Map<String, Integer> nameWeight = new HashMap<>();
        for(int i = 0; i < potentList.size(); i++){
            nameList.add(potentList.get(i).getUsername());
            if(!nameWeight.containsKey(potentList.get(i).getUsername())){
                //if name doesnt exist yet, weight of 2 because
                //at least 1 shared class
                nameWeight.put(potentList.get(i).getUsername(), 2);
            }
            else{
                int newWeight = nameWeight.get(potentList.get(i).getUsername()) + 2;
                nameWeight.replace(potentList.get(i).getUsername(), newWeight);
            }

        }
        nameList.remove(name);
        //remove duplicates by feeding into a Set
        String[] nameArr = nameList.toArray(new String[0]);
        for(int i = 0; i < nameArr.length; i++){
            reccs.add(userRepository.findByName(nameArr[i]).get());
        }
        //sort reccs by map values
        for(int i = 0; i < nameArr.length; i++) {
            for(int j = nameArr.length-1; j > i; j--){
                if(nameWeight.get(reccs.get(j).getName()) > nameWeight.get(reccs.get(i).getName())){
                    User u = reccs.get(i);
                    reccs.remove(i);
                    reccs.add(i, reccs.get(j-1));
                    reccs.remove(j);
                    reccs.add(j, u);
                }

            }

        }

        return reccs;
    }

}
