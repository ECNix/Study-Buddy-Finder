package study.buddy.api.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public Optional<Session> findSessionByID(Long sessionId) {
        return sessionRepository.findById(sessionId);
    }
    public List<Session> getUserSessions(String username){ return sessionRepository.findSessionsByOwner(username); }
    public List<Session> getAllSessions() { return sessionRepository.findAll(); }

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }
    public void removeSession(Long id) {
         sessionRepository.deleteById(id);
    }

}
