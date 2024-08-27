package com.bervan.interviewapp.interviewquestions;

import com.bervan.common.service.BaseService;
import com.bervan.core.model.BervanLogger;
import com.bervan.ieentities.ExcelIEEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InterviewQuestionService implements BaseService<Question> {
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
    public Set<Question> load() {
        return new HashSet<>(repository.findAll());
    }

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

    public List<Question> findByDifficultyNotSpringSecurity(Integer difficulty) {
        return repository.findAllByDifficultyAndTagsIn(difficulty, Arrays.asList("Java/DB/Testing", "Frameworks"));
    }
}
