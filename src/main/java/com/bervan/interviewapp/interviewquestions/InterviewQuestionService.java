package com.bervan.interviewapp.interviewquestions;

import com.bervan.common.service.AuthService;
import com.bervan.common.service.BaseService;
import com.bervan.core.model.BervanLogger;
import com.bervan.ieentities.ExcelIEEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterviewQuestionService implements BaseService<UUID, Question> {
    private final InterviewQuestionRepository repository;
    private final InterviewQuestionHistoryRepository historyRepository;
    private final BervanLogger logger;

    public InterviewQuestionService(InterviewQuestionRepository repository, InterviewQuestionHistoryRepository historyRepository, BervanLogger logger) {
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
    @PostFilter("filterObject.owner != null && filterObject.owner.getId().equals(T(com.bervan.common.service.AuthService).getLoggedUserId())")
    public Set<Question> load() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public void delete(Question item) {
        repository.delete(item);
    }

    @PostFilter("filterObject.owner != null && filterObject.owner.getId().equals(T(com.bervan.common.service.AuthService).getLoggedUserId())")
    public List<HistoryQuestion> loadHistory() {
        return historyRepository.findAll();
    }

    public void saveIfValid(List<? extends ExcelIEEntity> objects) {
        List<? extends ExcelIEEntity> list = objects.stream().filter(e -> e instanceof Question).toList();
        logger.debug("Filtered Interview Questions to be imported: " + list.size());
        for (ExcelIEEntity excelIEEntity : list) {
            repository.save(((Question) excelIEEntity));
        }
    }

    @PostFilter("filterObject.owner != null && filterObject.owner.getId().equals(T(com.bervan.common.service.AuthService).getLoggedUserId())")
    public List<Question> findByDifficultyNotSpringSecurity(Integer difficulty) {
        return repository.findAllByDifficultyAndTagsInAndOwnerId(difficulty, Arrays.asList("Java/DB/Testing", "Frameworks"), AuthService.getLoggedUserId());
    }
}
