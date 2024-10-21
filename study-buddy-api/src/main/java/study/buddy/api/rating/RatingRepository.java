package study.buddy.api.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByTutorName(String tutorName);

    Optional<Rating> findBySessionIDAndTutorNameAndRaterName(Long sessionID, String tutorName, String raterName);

    List<Rating> findAll();

    @Query(value="SELECT AVG(rating) from tutor_rating where tutor=?1 group by tutor", nativeQuery = true)
    Double averageTutorRating(String tutorName);
}
