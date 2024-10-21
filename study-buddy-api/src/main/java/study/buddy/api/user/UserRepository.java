package study.buddy.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);
    List<User> findByNameLike(String username);
    List<User> findByNameLikeAndSchool(String username, String school);

    Long countByName(String username);

    @Query(value="SELECT username, email_address, profile_info, user_type from user where username=?1", nativeQuery = true)
    Map<String, Object> getUserSafe(String username);
}