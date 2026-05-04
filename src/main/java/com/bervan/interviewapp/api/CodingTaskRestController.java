package com.bervan.interviewapp.api;

import com.bervan.common.config.EntityConfigValidator;
import com.bervan.common.controller.BaseOwnedController;
import com.bervan.common.mapper.BervanDTOMapper;
import com.bervan.common.service.AuthService;
import com.bervan.interviewapp.codingtask.CodingTaskService;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@PermitAll
@RequestMapping("/api/interview/coding-tasks")
public class CodingTaskRestController extends BaseOwnedController {

    protected CodingTaskRestController(CodingTaskService service, BervanDTOMapper mapper, EntityConfigValidator validator) {
        super(service, mapper, validator, "CodingTask");
    }

    @GetMapping
    public ResponseEntity<Page<CodingTaskDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.load(page, size, CodingTaskDto.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodingTaskDto> getById(@PathVariable UUID id) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.getById(id, CodingTaskDto.class);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CodingTaskDto req) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.create(req);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody CodingTaskDto req) {
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
