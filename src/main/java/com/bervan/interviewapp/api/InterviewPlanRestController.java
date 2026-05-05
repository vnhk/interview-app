package com.bervan.interviewapp.api;

import com.bervan.common.onevalue.OneValue;
import com.bervan.common.onevalue.OneValueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview/plan")
public class InterviewPlanRestController {

    private static final String PLAN_KEY = "interview-plan";

    private final OneValueService oneValueService;

    public InterviewPlanRestController(OneValueService oneValueService) {
        this.oneValueService = oneValueService;
    }

    @GetMapping
    public ResponseEntity<PlanResponse> getPlan() {
        List<OneValue> values = oneValueService.loadByKey(PLAN_KEY);
        String content = values.isEmpty() ? "" : (values.get(0).getContent() != null ? values.get(0).getContent() : "");
        return ResponseEntity.ok(new PlanResponse(content));
    }

    @PutMapping
    public ResponseEntity<PlanResponse> savePlan(@RequestBody PlanRequest req) {
        List<OneValue> values = oneValueService.loadByKey(PLAN_KEY);
        OneValue item = values.isEmpty() ? new OneValue() : values.get(0);
        item.setName(PLAN_KEY);
        item.setContent(req.getContent() != null ? req.getContent() : "");
        oneValueService.save(item);
        return ResponseEntity.ok(new PlanResponse(item.getContent()));
    }
}
