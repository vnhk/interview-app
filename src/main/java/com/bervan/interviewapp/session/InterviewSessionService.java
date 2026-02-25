package com.bervan.interviewapp.session;

import com.bervan.common.search.SearchService;
import com.bervan.common.service.AuthService;
import com.bervan.common.service.BaseService;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InterviewSessionService extends BaseService<UUID, InterviewSession> {
    private final InterviewSessionRepository repository;
    private final InterviewSessionHistoryRepository historyRepository;
    private final InterviewSessionQuestionRepository questionRepository;

    public InterviewSessionService(InterviewSessionRepository repository,
                                   InterviewSessionHistoryRepository historyRepository,
                                   InterviewSessionQuestionRepository questionRepository,
                                   SearchService searchService) {
        super(repository, searchService);
        this.repository = repository;
        this.historyRepository = historyRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void save(List<InterviewSession> data) {
        repository.saveAll(data);
    }

    public InterviewSession save(InterviewSession session) {
        return repository.save(session);
    }

    @Override
    public void delete(InterviewSession item) {
        repository.delete(item);
    }

    /**
     * Saves session and all its questions through the history mechanism.
     * Questions are saved first (as detached entities) so their history is correctly
     * captured before the session's cascade merge overwrites them.
     */
    @Transactional
    public void saveWithHistory(InterviewSession detachedSession) {
        if (detachedSession.getSessionQuestions() != null) {
            for (InterviewSessionQuestion q : detachedSession.getSessionQuestions()) {
                questionRepository.save(q);
            }
        }
        repository.save(detachedSession);
    }

    /**
     * Auto-save: checks that session is still IN_PROGRESS in DB (ghost scheduler protection),
     * then delegates to saveWithHistory.
     */
    @Transactional
    public void autoSave(UUID sessionId, String notes, InterviewSession detachedSession) {
        Optional<InterviewSession> opt = repository.findById(sessionId);
        if (opt.isEmpty() || !"IN_PROGRESS".equals(opt.get().getStatus())) {
            return;
        }
        detachedSession.setNotes(notes);
        saveWithHistory(detachedSession);
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<HistoryInterviewSession> loadHistory() {
        return historyRepository.findAll();
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<InterviewSession> loadAllSessions() {
        return repository.findAllByOwnersId(AuthService.getLoggedUserId());
    }
}
