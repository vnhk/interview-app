package com.bervan.interviewapp.interviewquestions;

import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class InterviewQuestionService implements BaseService<Question> {
    private final InterviewQuestionRepository repository;
    private final InterviewQuestionHistoryRepository historyRepository;

    public InterviewQuestionService(InterviewQuestionRepository repository, InterviewQuestionHistoryRepository historyRepository) {
        this.repository = repository;
        this.historyRepository = historyRepository;
    }

    @Override
    public void save(List<Question> data) {
        repository.saveAll(data);
    }

    public void save(Question codingTask) {
        repository.save(codingTask);
    }

    @Override
    public List<Question> load() {
        return repository.findAll();
    }

    public List<HistoryQuestion> loadHistory() {
        return historyRepository.findAll();
    }

    public void saveIfValid(List<? extends ExcelIEEntity> objects) {
        List<? extends ExcelIEEntity> list = objects.stream().filter(e -> e instanceof Question).toList();
        for (ExcelIEEntity excelIEEntity : list) {
            repository.save(((Question) excelIEEntity));
        }
    }

    public List<Question> findByDifficultyNotSpringSecurity(Integer difficulty) {
        return repository.findAllByDifficultyAndTagsIn(difficulty, Arrays.asList("Java/DB/Testing", "Frameworks"));
    }
}
