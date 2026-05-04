package com.bervan.interviewapp.api;

import com.bervan.common.config.EntityConfigValidator;
import com.bervan.common.controller.BaseOwnedController;
import com.bervan.common.mapper.BervanDTOMapper;
import com.bervan.common.onevalue.OneValue;
import com.bervan.common.onevalue.OneValueService;
import com.bervan.common.service.AuthService;
import com.bervan.interviewapp.codingtask.CodingTask;
import com.bervan.interviewapp.codingtask.CodingTaskService;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.interviewapp.session.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@PermitAll
@RequestMapping("/api/interview/sessions")
public class InterviewSessionRestController extends BaseOwnedController {

    private final InterviewSessionService sessionService;
    private final InterviewQuestionService questionService;
    private final CodingTaskService codingTaskService;
    private final OneValueService oneValueService;

    protected InterviewSessionRestController(
            InterviewSessionService service,
            BervanDTOMapper mapper,
            EntityConfigValidator validator,
            InterviewQuestionService questionService,
            CodingTaskService codingTaskService,
            OneValueService oneValueService) {
        super(service, mapper, validator, "InterviewSession");
        this.sessionService = service;
        this.questionService = questionService;
        this.codingTaskService = codingTaskService;
        this.oneValueService = oneValueService;
    }

    @GetMapping
    public ResponseEntity<Page<InterviewSessionDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.load(page, size, InterviewSessionDto.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewSessionDetailDto> getById(@PathVariable UUID id) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Optional<InterviewSession> opt = sessionService.loadById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDetailDto(opt.get()));
    }

    @PostMapping
    public ResponseEntity<InterviewSessionDetailDto> create(@RequestBody CreateInterviewSessionRequest req) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        InterviewSession session = new InterviewSession();
        session.setId(UUID.randomUUID());
        session.setCandidateName(req.getCandidateName() != null ? req.getCandidateName() : "Unknown");
        session.setConfigName(req.getConfigName() != null ? req.getConfigName() : "");
        session.setMainTags(req.getMainTags() != null ? req.getMainTags() : "");
        session.setSecondaryTags(req.getSecondaryTags() != null ? req.getSecondaryTags() : "");
        session.setSkillLevelConfig(req.getSkillLevelConfig() != null ? req.getSkillLevelConfig() : "");
        session.setStatus("IN_PROGRESS");
        session.setNotes("");
        session.setTotalQuestions(req.getQuestionIds() != null ? req.getQuestionIds().size() : 0);

        String planTemplate = "";
        List<OneValue> planValues = oneValueService.loadByKey("interview-plan");
        if (!planValues.isEmpty() && planValues.get(0).getContent() != null) {
            planTemplate = planValues.get(0).getContent();
        }
        session.setPlanTemplate(planTemplate);

        List<InterviewSessionQuestion> sessionQuestions = new ArrayList<>();
        if (req.getQuestionIds() != null) {
            for (int i = 0; i < req.getQuestionIds().size(); i++) {
                UUID qId = req.getQuestionIds().get(i);
                Optional<Question> question = questionService.loadById(qId);
                if (question.isPresent()) {
                    InterviewSessionQuestion sq = new InterviewSessionQuestion();
                    sq.setId(UUID.randomUUID());
                    sq.setQuestionNumber(i + 1);
                    sq.setQuestion(question.get());
                    sq.setAnswerStatus("NOT_ASKED");
                    sq.setScore(0.0);
                    sq.setNotes("");
                    sq.setSession(session);
                    sessionQuestions.add(sq);
                }
            }
        }
        session.setSessionQuestions(sessionQuestions);

        List<InterviewSessionCodingTask> sessionCodingTasks = new ArrayList<>();
        if (req.getCodingTaskIds() != null) {
            for (int i = 0; i < req.getCodingTaskIds().size(); i++) {
                UUID ctId = req.getCodingTaskIds().get(i);
                Optional<CodingTask> task = codingTaskService.loadById(ctId);
                if (task.isPresent()) {
                    InterviewSessionCodingTask sct = new InterviewSessionCodingTask();
                    sct.setId(UUID.randomUUID());
                    sct.setTaskNumber(i + 1);
                    sct.setCodingTask(task.get());
                    sct.setScore(0.0);
                    sct.setNotes("");
                    sct.setSession(session);
                    sessionCodingTasks.add(sct);
                }
            }
        }
        session.setSessionCodingTasks(sessionCodingTasks);

        InterviewSession saved = sessionService.save(session);
        Optional<InterviewSession> reloaded = sessionService.loadById(saved.getId());
        return ResponseEntity.ok(toDetailDto(reloaded.orElse(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewSessionDetailDto> update(@PathVariable UUID id, @RequestBody UpdateSessionRequest req) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<InterviewSession> opt = sessionService.loadById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        InterviewSession session = opt.get();

        if (req.getNotes() != null) session.setNotes(req.getNotes());
        if (req.getStatus() != null) session.setStatus(req.getStatus());
        if (req.getFeedback() != null) session.setFeedback(req.getFeedback());

        if (req.getSessionQuestions() != null) {
            Map<UUID, InterviewSessionQuestion> sqMap = session.getSessionQuestions().stream()
                    .collect(Collectors.toMap(InterviewSessionQuestion::getId, sq -> sq));
            for (SessionQuestionUpdate qu : req.getSessionQuestions()) {
                InterviewSessionQuestion sq = sqMap.get(qu.getId());
                if (sq != null) {
                    if (qu.getScore() != null) sq.setScore(qu.getScore());
                    if (qu.getNotes() != null) sq.setNotes(qu.getNotes());
                    if (qu.getAnswerStatus() != null) sq.setAnswerStatus(qu.getAnswerStatus());
                }
            }
        }

        if (req.getSessionCodingTasks() != null) {
            Map<UUID, InterviewSessionCodingTask> sctMap = session.getSessionCodingTasks().stream()
                    .collect(Collectors.toMap(InterviewSessionCodingTask::getId, sct -> sct));
            for (SessionCodingTaskUpdate ctu : req.getSessionCodingTasks()) {
                InterviewSessionCodingTask sct = sctMap.get(ctu.getId());
                if (sct != null) {
                    if (ctu.getScore() != null) sct.setScore(ctu.getScore());
                    if (ctu.getNotes() != null) sct.setNotes(ctu.getNotes());
                }
            }
        }

        sessionService.saveWithHistory(session);
        Optional<InterviewSession> reloaded = sessionService.loadById(id);
        return ResponseEntity.ok(toDetailDto(reloaded.orElse(session)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return super.delete(id);
    }

    private InterviewSessionDetailDto toDetailDto(InterviewSession session) {
        InterviewSessionDetailDto dto = new InterviewSessionDetailDto();
        dto.setId(session.getId());
        dto.setCandidateName(session.getCandidateName());
        dto.setConfigName(session.getConfigName());
        dto.setStatus(session.getStatus());
        dto.setTotalQuestions(session.getTotalQuestions());
        dto.setMainTags(session.getMainTags());
        dto.setSecondaryTags(session.getSecondaryTags());
        dto.setNotes(session.getNotes());
        dto.setPlanTemplate(session.getPlanTemplate());
        dto.setFeedback(session.getFeedback());
        dto.setSkillLevelConfig(session.getSkillLevelConfig());
        dto.setModificationDate(session.getModificationDate());

        List<InterviewSessionQuestionDto> questionDtos = session.getSessionQuestions() == null
                ? new ArrayList<>()
                : session.getSessionQuestions().stream()
                .filter(sq -> sq != null && sq.getId() != null)
                .sorted(Comparator.comparing(InterviewSessionQuestion::getQuestionNumber, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(sq -> {
                    InterviewSessionQuestionDto qDto = new InterviewSessionQuestionDto();
                    qDto.setId(sq.getId());
                    qDto.setQuestionNumber(sq.getQuestionNumber());
                    qDto.setScore(sq.getScore());
                    qDto.setNotes(sq.getNotes());
                    qDto.setAnswerStatus(sq.getAnswerStatus());
                    Question q = sq.getQuestion();
                    if (q != null) {
                        qDto.setQuestionId(q.getId());
                        qDto.setQuestionName(q.getName());
                        qDto.setQuestionTags(q.getTags());
                        qDto.setQuestionDifficulty(q.getDifficulty());
                        qDto.setQuestionDetails(q.getQuestionDetails());
                        qDto.setAnswerDetails(q.getAnswerDetails());
                        qDto.setMaxPoints(q.getMaxPoints());
                    }
                    return qDto;
                })
                .collect(Collectors.toList());
        dto.setSessionQuestions(questionDtos);

        List<InterviewSessionCodingTaskDto> codingTaskDtos = session.getSessionCodingTasks() == null
                ? new ArrayList<>()
                : session.getSessionCodingTasks().stream()
                .filter(sct -> sct != null && sct.getId() != null)
                .sorted(Comparator.comparing(InterviewSessionCodingTask::getTaskNumber, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(sct -> {
                    InterviewSessionCodingTaskDto ctDto = new InterviewSessionCodingTaskDto();
                    ctDto.setId(sct.getId());
                    ctDto.setTaskNumber(sct.getTaskNumber());
                    ctDto.setScore(sct.getScore());
                    ctDto.setNotes(sct.getNotes());
                    CodingTask ct = sct.getCodingTask();
                    if (ct != null) {
                        ctDto.setCodingTaskId(ct.getId());
                        ctDto.setCodingTaskName(ct.getName());
                        ctDto.setInitialCode(ct.getInitialCode());
                        ctDto.setExampleCode(ct.getExampleCode());
                        ctDto.setExampleCodeDetails(ct.getExampleCodeDetails());
                        ctDto.setTaskQuestions(ct.getQuestions());
                    }
                    return ctDto;
                })
                .collect(Collectors.toList());
        dto.setSessionCodingTasks(codingTaskDtos);

        return dto;
    }
}
