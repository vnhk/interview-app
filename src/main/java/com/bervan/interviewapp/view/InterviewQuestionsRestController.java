package com.bervan.interviewapp.view;

import com.bervan.common.config.EntityConfigValidator;
import com.bervan.common.controller.BaseOwnedController;
import com.bervan.common.mapper.BervanDTOMapper;
import com.bervan.common.service.AuthService;
import com.bervan.interviewapp.api.InterviewQuestionDto;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@PermitAll
@RequestMapping("/api/interview/questions")
public class InterviewQuestionsRestController extends BaseOwnedController {

    private final InterviewQuestionService questionService;

    protected InterviewQuestionsRestController(InterviewQuestionService service, BervanDTOMapper mapper, EntityConfigValidator validator) {
        super(service, mapper, validator, "Question");
        this.questionService = service;
    }

    @GetMapping("/tags")
    public ResponseEntity<List<String>> getTags() {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Set<String> tags = questionService.getAllDistinctTags();
        return ResponseEntity.ok(tags.stream().sorted().collect(Collectors.toList()));
    }

    @GetMapping("/by-tag-difficulty")
    public ResponseEntity<List<InterviewQuestionDto>> getByTagAndDifficulty(
            @RequestParam String tag,
            @RequestParam Integer difficulty) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        List<Question> questions = questionService.findByTagsAndDifficulty(List.of(tag), difficulty);
        List<InterviewQuestionDto> dtos = questions.stream()
                .map(q -> mapper.map(q, InterviewQuestionDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewQuestionDto> getById(@PathVariable UUID id) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.getById(id, InterviewQuestionDto.class);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InterviewQuestionDto req) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.create(req);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody InterviewQuestionDto req) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        req.setId(id);
        return super.update(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.delete(id);
    }

    @GetMapping
    public ResponseEntity<Page<InterviewQuestionDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) throws Exception {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.load(page, size, InterviewQuestionDto.class);
    }
}
