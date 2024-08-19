package com.bervan.interviewapp.view;

import com.bervan.common.AbstractOneValueView;
import com.bervan.common.onevalue.OneValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractInterviewPlanView extends AbstractOneValueView {
    public static final String ROUTE_NAME = "interview-app/interview-plan";


    public AbstractInterviewPlanView(@Autowired OneValueService service) {
        super(new InterviewAppPageLayout(ROUTE_NAME), "interview-plan", "Interview Plan", service);
    }
}