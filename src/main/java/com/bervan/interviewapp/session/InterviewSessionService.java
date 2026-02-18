package com.bervan.interviewapp.session;

import com.bervan.common.search.SearchService;
import com.bervan.common.service.AuthService;
import com.bervan.common.service.BaseService;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InterviewSessionService extends BaseService<UUID, InterviewSession> {
    private final InterviewSessionRepository repository;
    private final InterviewSessionHistoryRepository historyRepository;

    public InterviewSessionService(InterviewSessionRepository repository, InterviewSessionHistoryRepository historyRepository, SearchService searchService) {
        super(repository, searchService);
        this.repository = repository;
        this.historyRepository = historyRepository;
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

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<HistoryInterviewSession> loadHistory() {
        return historyRepository.findAll();
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<InterviewSession> loadAllSessions() {
        return repository.findAllByOwnersId(AuthService.getLoggedUserId());
    }
}
