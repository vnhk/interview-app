package com.bervan.interviewapp.api;

import com.bervan.common.config.EntityConfigValidator;
import com.bervan.common.controller.BaseOwnedController;
import com.bervan.common.controller.ImportResult;
import com.bervan.common.mapper.BervanDTOMapper;
import com.bervan.interviewapp.codingtask.CodingTaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/interview/coding-tasks")
public class CodingTaskRestController extends BaseOwnedController {

    protected CodingTaskRestController(CodingTaskService service, BervanDTOMapper mapper, EntityConfigValidator validator) {
        super(service, mapper, validator, "CodingTask");
    }

    @GetMapping
    public ResponseEntity<Page<CodingTaskDto>> list(
            @RequestParam MultiValueMap<String, String> allParams,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        return super.search(allParams, page, size, CodingTaskDto.class, com.bervan.interviewapp.codingtask.CodingTask.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodingTaskDto> getById(@PathVariable UUID id) {
        return super.getById(id, CodingTaskDto.class);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CodingTaskDto req) {
        return super.create(req);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody CodingTaskDto req) {
        req.setId(id);
        return super.update(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return super.delete(id);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam MultiValueMap<String, String> allParams) {
        return super.exportAll(allParams, CodingTaskDto.class, "coding-tasks", com.bervan.interviewapp.codingtask.CodingTask.class);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportResult> importData(@RequestParam("file") MultipartFile file) {
        return super.importAll(file, CodingTaskDto.class);
    }
}
