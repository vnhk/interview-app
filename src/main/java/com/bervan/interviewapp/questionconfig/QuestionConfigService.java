package com.bervan.interviewapp.questionconfig;

import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionConfigService implements BaseService<QuestionConfig> {
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

    public void save(QuestionConfig questionConfig) {
        repository.save(questionConfig);
    }

    @Override
    public List<QuestionConfig> load() {
        return repository.findAll();
    }

    public List<HistoryQuestionConfig> loadHistory() {
        return historyRepository.findAll();
    }

    public void saveIfValid(List<? extends ExcelIEEntity> objects) {
        List<? extends ExcelIEEntity> list = objects.stream().filter(e -> e instanceof QuestionConfig).toList();
        for (ExcelIEEntity excelIEEntity : list) {
            repository.save(((QuestionConfig) excelIEEntity));
        }
    }

    public Optional<QuestionConfig> loadByName(String selectedLvl) {
        return repository.findByName(selectedLvl);
    }
}
