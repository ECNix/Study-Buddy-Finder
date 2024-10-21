package study.buddy.api.participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, ParticipantID> {
    List<Participant> findAllBySessionIDAndIsTutorTrue(Long sessionID);
    List<Participant> findAllBySessionIDAndIsTutorFalse(Long sessionID);
    List<Participant> findAllBySessionID(Long sessionID);

    Optional<Participant> findByUsernameAndSessionID(String username, Long sessionID);
    List<Participant> findByUsername(String username);

    Long deleteByUsernameAndSessionID(String username, Long sessionID);
    Long deleteBySessionID(Long sessionID);
}