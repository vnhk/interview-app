package com.bervan.interviewapp.interviewquestions;

import com.bervan.common.search.SearchService;
import com.bervan.common.service.AuthService;
import com.bervan.common.service.BaseService;
import com.bervan.ieentities.ExcelIEEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewQuestionService extends BaseService<UUID, Question> {
    private final InterviewQuestionRepository repository;
    private final InterviewQuestionHistoryRepository historyRepository;

    public InterviewQuestionService(InterviewQuestionRepository repository, InterviewQuestionHistoryRepository historyRepository, SearchService searchService) {
        super(repository, searchService);
        this.repository = repository;
        this.historyRepository = historyRepository;
    }

    @Override
    public void save(List<Question> data) {
        repository.saveAll(data);
    }

    public Question save(Question question) {
        return repository.save(question);
    }

    @Override
    public void delete(Question item) {
        repository.delete(item);
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<HistoryQuestion> loadHistory() {
        return historyRepository.findAll();
    }

    public List<Question> findByTagsAndDifficulty(List<String> tags, int difficulty) {
        List<Question> allByDifficulty = repository.findAllByDifficultyAndOwnersId(difficulty, AuthService.getLoggedUserId());
        return allByDifficulty.stream()
                .filter(q -> hasAnyTag(q, tags))
                .collect(Collectors.toList());
    }

    public List<Question> findByTags(List<String> tags) {
        List<Question> all = repository.findAllByOwnersId(AuthService.getLoggedUserId());
        return all.stream()
                .filter(q -> hasAnyTag(q, tags))
                .collect(Collectors.toList());
    }

    public Set<String> getAllDistinctTags() {
        List<Question> all = repository.findAllByOwnersId(AuthService.getLoggedUserId());
        Set<String> tags = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Question q : all) {
            if (q.getTags() != null && !q.getTags().isBlank()) {
                for (String tag : q.getTags().split(",")) {
                    String trimmed = tag.trim();
                    if (!trimmed.isEmpty()) {
                        tags.add(trimmed);
                    }
                }
            }
        }
        return tags;
    }

    private boolean hasAnyTag(Question question, List<String> tags) {
        if (question.getTags() == null || question.getTags().isBlank()) {
            return false;
        }
        Set<String> questionTags = Arrays.stream(question.getTags().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return tags.stream()
                .map(String::toLowerCase)
                .anyMatch(questionTags::contains);
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<Question> findByDifficultyNotSpringSecurity(Integer difficulty) {
        return repository.findAllByDifficultyAndOwnersId(difficulty, AuthService.getLoggedUserId())
                .stream()
                .filter(q -> !isSecurityQuestion(q))
                .collect(Collectors.toList());
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<Question> findByDifficultySpringSecurity() {
        return repository.findAllByOwnersId(AuthService.getLoggedUserId())
                .stream()
                .filter(this::isSecurityQuestion)
                .collect(Collectors.toList());
    }

    private boolean isSecurityQuestion(Question q) {
        return q.getTags() != null && q.getTags().toLowerCase().contains("security");
    }
}
