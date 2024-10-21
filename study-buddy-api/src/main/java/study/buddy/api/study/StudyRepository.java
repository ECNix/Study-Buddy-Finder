package study.buddy.api.study;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<Study, StudyID> {
    List<Study> findByUsername(String username);
    Optional<Study> findByUsernameAndCourseID(String username, Long courseID);
    List<Study> findByCourseID(Long courseID);

    Long removeByUsernameAndCourseID(String username, Long courseID);

}