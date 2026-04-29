package com.bervan.interviewapp.view;

import com.bervan.common.config.EntityConfigValidator;
import com.bervan.common.controller.BaseOwnedController;
import com.bervan.common.mapper.BervanDTOMapper;
import com.bervan.common.service.AuthService;
import com.bervan.core.model.BaseDTO;
import com.bervan.core.model.BaseModel;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.logging.JsonLogger;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@PermitAll
@RequestMapping("/api/interview/questions")
public class InterviewQuestionsController extends BaseOwnedController {
    private final JsonLogger log = JsonLogger.getLogger(getClass(), "interview-app");

    protected InterviewQuestionsController(InterviewQuestionService service, BervanDTOMapper mapper, EntityConfigValidator validator) {
        super(service, mapper, validator, "Question");
    }


    @GetMapping("/{id}")
    public ResponseEntity<InterviewQuestionDto> getById(@PathVariable UUID id) {
        if (AuthService.getLoggedUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return super.getById(id, InterviewQuestionDto.class);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InterviewQuestionDto req) {
        if (AuthService.getLoggedUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return super.create(req);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody InterviewQuestionDto req) {
        if (AuthService.getLoggedUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (AuthService.getLoggedUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return super.delete(id);
    }


    @GetMapping
    public ResponseEntity<Page<InterviewQuestionDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) throws Exception {
        if (AuthService.getLoggedUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return super.load(page, size, InterviewQuestionDto.class);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class InterviewQuestionDto implements BaseDTO<UUID> {
        private UUID id;
        private String name;
        private String tags;
        private Integer difficulty;
        private String questionDetails;
        private String answerDetails;
        private Double maxPoints;

        public InterviewQuestionDto() {

        }

        @Override
        public Class<? extends BaseModel<UUID>> dtoTarget() {
            return Question.class;
        }
    }
}
