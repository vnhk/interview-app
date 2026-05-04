package com.bervan.interviewapp.api;

import com.bervan.common.config.EntityConfigValidator;
import com.bervan.common.controller.BaseOwnedController;
import com.bervan.common.mapper.BervanDTOMapper;
import com.bervan.common.service.AuthService;
import com.bervan.interviewapp.questionconfig.QuestionConfigService;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@PermitAll
@RequestMapping("/api/interview/question-configs")
public class QuestionConfigRestController extends BaseOwnedController {

    protected QuestionConfigRestController(QuestionConfigService service, BervanDTOMapper mapper, EntityConfigValidator validator) {
        super(service, mapper, validator, "QuestionConfig");
    }

    @GetMapping
    public ResponseEntity<Page<QuestionConfigDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.load(page, size, QuestionConfigDto.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionConfigDto> getById(@PathVariable UUID id) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.getById(id, QuestionConfigDto.class);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody QuestionConfigDto req) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.create(req);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody QuestionConfigDto req) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        req.setId(id);
        return super.update(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.delete(id);
    }
}
