package com.bervan.interviewapp.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AbstractInterviewHomeView extends VerticalLayout {

    public static final String ROUTE_NAME = "interview-app/home";
    private final InterviewAppPageLayout pageLayout;

    public AbstractInterviewHomeView() {
        pageLayout = new InterviewAppPageLayout(ROUTE_NAME);
        add(pageLayout);
    }
}
