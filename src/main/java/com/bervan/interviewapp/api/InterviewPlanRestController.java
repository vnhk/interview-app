package com.bervan.interviewapp.api;

import com.bervan.common.onevalue.OneValue;
import com.bervan.common.onevalue.OneValueService;
import com.bervan.common.service.AuthService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PermitAll
@RequestMapping("/api/interview/plan")
public class InterviewPlanRestController {

    private static final String PLAN_KEY = "interview-plan";

    private final OneValueService oneValueService;

    public InterviewPlanRestController(OneValueService oneValueService) {
        this.oneValueService = oneValueService;
    }

    @GetMapping
    public ResponseEntity<PlanResponse> getPlan() {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        List<OneValue> values = oneValueService.loadByKey(PLAN_KEY);
        String content = values.isEmpty() ? "" : (values.get(0).getContent() != null ? values.get(0).getContent() : "");
        return ResponseEntity.ok(new PlanResponse(content));
    }

    @PutMapping
    public ResponseEntity<PlanResponse> savePlan(@RequestBody PlanRequest req) {
        if (AuthService.getLoggedUserId() == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        List<OneValue> values = oneValueService.loadByKey(PLAN_KEY);
        OneValue item = values.isEmpty() ? new OneValue() : values.get(0);
        item.setName(PLAN_KEY);
        item.setContent(req.getContent() != null ? req.getContent() : "");
        oneValueService.save(item);
        return ResponseEntity.ok(new PlanResponse(item.getContent()));
    }
}
