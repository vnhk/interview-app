package com.bervan.interviewapp.questionconfig;

import com.bervan.common.service.AuthService;
import com.bervan.common.service.BaseService;
import com.bervan.ieentities.ExcelIEEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionConfigService implements BaseService<UUID, QuestionConfig> {
    private final QuestionConfigRepository repository;
    private final QuestionConfigHistoryRepository historyRepository;

    public QuestionConfigService(QuestionConfigRepository repository, QuestionConfigHistoryRepository historyRepository) {
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
    @PostFilter("filterObject.owner != null && filterObject.owner.getId().equals(T(com.bervan.common.service.AuthService).getLoggedUserId())")
    public Set<QuestionConfig> load() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public void delete(QuestionConfig item) {
        repository.delete(item);
    }

    @PostFilter("filterObject.owner != null && filterObject.owner.getId().equals(T(com.bervan.common.service.AuthService).getLoggedUserId())")
    public List<HistoryQuestionConfig> loadHistory() {
        return historyRepository.findAll();
    }

    public void saveIfValid(List<? extends ExcelIEEntity> objects) {
        List<? extends ExcelIEEntity> list = objects.stream().filter(e -> e instanceof QuestionConfig).toList();
        for (ExcelIEEntity excelIEEntity : list) {
            repository.save(((QuestionConfig) excelIEEntity));
        }
    }

    @PostFilter("filterObject.owner != null && filterObject.owner.getId().equals(T(com.bervan.common.service.AuthService).getLoggedUserId())")
    public Optional<QuestionConfig> loadByName(String selectedLvl) {
        return repository.findByNameAndOwnerId(selectedLvl, AuthService.getLoggedUserId());
    }
}
