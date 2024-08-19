package com.bervan.interviewapp.codingtask;

import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodingTaskService implements BaseService<CodingTask> {
    private final CodingTaskRepository repository;
    private final CodingTaskHistoryRepository historyRepository;

    public CodingTaskService(CodingTaskRepository repository, CodingTaskHistoryRepository historyRepository) {
        this.repository = repository;
        this.historyRepository = historyRepository;
    }

    @Override
    public void save(List<CodingTask> data) {
        repository.saveAll(data);
    }

    public void save(CodingTask codingTask) {
        repository.save(codingTask);
    }

    @Override
    public List<CodingTask> load() {
        return repository.findAll();
    }

    public List<HistoryCodingTask> loadHistory() {
        return historyRepository.findAll();
    }

    public void saveIfValid(List<? extends ExcelIEEntity> objects) {
        List<? extends ExcelIEEntity> list = objects.stream().filter(e -> e instanceof CodingTask).toList();
        for (ExcelIEEntity excelIEEntity : list) {
            repository.save(((CodingTask) excelIEEntity));
        }
    }
}
