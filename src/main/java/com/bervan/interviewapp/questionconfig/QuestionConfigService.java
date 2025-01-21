package com.bervan.interviewapp.questionconfig;

import com.bervan.common.search.SearchService;
import com.bervan.common.service.AuthService;
import com.bervan.common.service.BaseService;
import com.bervan.ieentities.ExcelIEEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class QuestionConfigService extends BaseService<UUID, QuestionConfig> {
    private final QuestionConfigRepository repository;
    private final QuestionConfigHistoryRepository historyRepository;

    public QuestionConfigService(QuestionConfigRepository repository, QuestionConfigHistoryRepository historyRepository, SearchService searchService) {
        super(repository, searchService);
        this.repository = repository;
        this.historyRepository = historyRepository;
    }

    @Override
    public void save(List<QuestionConfig> data) {
        repository.saveAll(data);
    }

    public QuestionConfig save(QuestionConfig questionConfig) {
        return repository.save(questionConfig);
    }

    @Override
    public void delete(QuestionConfig item) {
        repository.delete(item);
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<HistoryQuestionConfig> loadHistory() {
        return historyRepository.findAll();
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<QuestionConfig> loadByName(String selectedLvl) {
        return repository.findByNameAndOwnersId(selectedLvl, AuthService.getLoggedUserId());
    }
}
