package com.bervan.interviewapp.codingtask;

import com.bervan.common.service.BaseService;
import com.bervan.core.model.BervanLogger;
import com.bervan.ieentities.ExcelIEEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CodingTaskService implements BaseService<CodingTask> {
    private final CodingTaskRepository repository;
    private final CodingTaskHistoryRepository historyRepository;
    private final BervanLogger logger;

    public CodingTaskService(CodingTaskRepository repository, CodingTaskHistoryRepository historyRepository, BervanLogger logger) {
        this.repository = repository;
        this.historyRepository = historyRepository;
        this.logger = logger;
    }

    @Override
    public void save(List<CodingTask> data) {
        repository.saveAll(data);
    }

    public CodingTask save(CodingTask codingTask) {
        return repository.save(codingTask);
    }

    @Override
    public Set<CodingTask> load() {
        return new HashSet<>(repository.findAll());
    }

    public List<HistoryCodingTask> loadHistory() {
        return historyRepository.findAll();
    }

    public void saveIfValid(List<? extends ExcelIEEntity> objects) {
        List<? extends ExcelIEEntity> list = objects.stream().filter(e -> e instanceof CodingTask).toList();
        logger.debug("Filtered Coding Tasks to be imported: " + list.size());
        for (ExcelIEEntity excelIEEntity : list) {
            repository.save(((CodingTask) excelIEEntity));
        }
    }
}
