package com.bervan.interviewapp.interviewquestions;

import com.bervan.common.search.SearchService;
import com.bervan.common.service.AuthService;
import com.bervan.common.service.BaseService;
import com.bervan.core.model.BervanLogger;
import com.bervan.ieentities.ExcelIEEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterviewQuestionService extends BaseService<UUID, Question> {
    private final InterviewQuestionRepository repository;
    private final InterviewQuestionHistoryRepository historyRepository;
    private final BervanLogger logger;

    public InterviewQuestionService(InterviewQuestionRepository repository, InterviewQuestionHistoryRepository historyRepository, BervanLogger logger, SearchService searchService) {
        super(repository, searchService);
        this.repository = repository;
        this.historyRepository = historyRepository;
        this.logger = logger;
    }

    @Override
    public void save(List<Question> data) {
        repository.saveAll(data);
    }

    public Question save(Question question) {
        return repository.save(question);
    }

    @Override
    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public Set<Question> load() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public void delete(Question item) {
        repository.delete(item);
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<HistoryQuestion> loadHistory() {
        return historyRepository.findAll();
    }


    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<Question> findByDifficultyNotSpringSecurity(Integer difficulty) {
        return repository.findAllByDifficultyAndTagsInAndOwnersId(difficulty, Arrays.asList("Java/DB/Testing", "Frameworks"), AuthService.getLoggedUserId());
    }
}
