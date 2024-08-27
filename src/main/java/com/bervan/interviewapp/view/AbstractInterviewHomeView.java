package com.bervan.interviewapp.view;

import com.bervan.common.AbstractPageView;

public abstract class AbstractInterviewHomeView extends AbstractPageView {

    public static final String ROUTE_NAME = "interview-app/home";
    private final InterviewAppPageLayout pageLayout;

    public AbstractInterviewHomeView() {
        pageLayout = new InterviewAppPageLayout(ROUTE_NAME);
        add(pageLayout);
    }
}
