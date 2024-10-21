package study.buddy.api.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<IdOnly> findByNameAndSchool(String name, String school);
    List<Course> findBySchool(String school);
}