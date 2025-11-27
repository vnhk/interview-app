package com.bervan.interviewapp.codingtask;

import com.bervan.common.search.SearchService;
import com.bervan.common.service.BaseService;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CodingTaskService extends BaseService<UUID, CodingTask> {
    private final CodingTaskRepository repository;
    private final CodingTaskHistoryRepository historyRepository;

    public CodingTaskService(CodingTaskRepository repository, SearchService searchService, CodingTaskHistoryRepository historyRepository) {
        super(repository, searchService);
        this.repository = repository;
        this.historyRepository = historyRepository;
    }

    @Override
    public void save(List<CodingTask> data) {
        repository.saveAll(data);
    }

    public CodingTask save(CodingTask codingTask) {
        return repository.save(codingTask);
    }

    @Override
    public void delete(CodingTask item) {
        repository.delete(item);
    }

    @PostFilter("(T(com.bervan.common.service.AuthService).hasAccess(filterObject.owners))")
    public List<HistoryCodingTask> loadHistory() {
        return historyRepository.findAll();
    }

}
