package com.bervan.interviewapp.api;

import com.bervan.common.config.EntityConfigValidator;
import com.bervan.common.controller.BaseOwnedController;
import com.bervan.common.controller.BaseOwnedController.ImportResult;
import com.bervan.common.mapper.BervanDTOMapper;
import com.bervan.interviewapp.questionconfig.QuestionConfigService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/interview/question-configs")
public class QuestionConfigRestController extends BaseOwnedController {

    protected QuestionConfigRestController(QuestionConfigService service, BervanDTOMapper mapper, EntityConfigValidator validator) {
        super(service, mapper, validator, "QuestionConfig");
    }

    @GetMapping
    public ResponseEntity<Page<QuestionConfigDto>> list(
            @RequestParam MultiValueMap<String, String> allParams,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return super.search(allParams, page, size, QuestionConfigDto.class, com.bervan.interviewapp.questionconfig.QuestionConfig.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionConfigDto> getById(@PathVariable UUID id) {
        return super.getById(id, QuestionConfigDto.class);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody QuestionConfigDto req) {
        return super.create(req);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody QuestionConfigDto req) {
        req.setId(id);
        return super.update(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return super.delete(id);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam MultiValueMap<String, String> allParams) {
        return super.exportAll(allParams, QuestionConfigDto.class, "question-configs", com.bervan.interviewapp.questionconfig.QuestionConfig.class);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportResult> importData(@RequestParam("file") MultipartFile file) {
        return super.importAll(file, QuestionConfigDto.class);
    }
}
