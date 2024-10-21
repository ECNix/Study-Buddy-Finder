package study.buddy.api.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;

    public List<Participant> getStudentsInSession(Long id){
        return participantRepository.findAllBySessionIDAndIsTutorFalse(id);
    }
    public List<Participant> getTutorsInSession(Long id){
        return participantRepository.findAllBySessionIDAndIsTutorTrue(id);
    }
    public List<Participant> getParticipantsInSession(Long id){
        return participantRepository.findAllBySessionID(id);
    }

    public Optional<Participant> getParticipant(String username, Long id){
        return participantRepository.findByUsernameAndSessionID(username, id);
    }
    public List<Participant> getParticipating(String username){
        return participantRepository.findByUsername(username);
    }

    public Participant addParticipant(Participant p){
        return participantRepository.save(p);
    }
    public Long removeParticipant(String username, Long id){
        return participantRepository.deleteByUsernameAndSessionID(username,id);
    }
    public Long removeSessionParticipants(Long id){
        return participantRepository.deleteBySessionID(id);
    }
}
