package study.buddy.api.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findAll();
    List<Friend> findByName(String name);
    List<Friend> findByNameAndStatus(String name, Friend.Status status);
    List<Friend> findByFriendName(String friendName);
    List<Friend> findByFriendNameAndStatus(String friendName, Friend.Status status);
    Optional<Friend> findByNameAndFriendName(String name, String friendName);
    Long deleteByNameAndFriendName(String name, String friendName);
    Optional<Friend> findById(Long id);
}