package com.bervan.interviewapp.view;

import com.bervan.common.view.AbstractHomePageView;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.List;

public abstract class AbstractInterviewHomeView extends AbstractHomePageView {

    public static final String ROUTE_NAME = "interview-app/home";

    public AbstractInterviewHomeView() {
        add(createHeader("Interview App"
                , "Welcome to the Interview App!"));

        add(createQuickAccessSection(
                List.of("Interview", "Interview Questions", "Coding Tasks", "Interview Plan", "Question Config", "Import/Export"),
                List.of("Start an interview!", "Browse interview questions!", "Browse coding tasks!", "Manage your interview plan!", "Configure question settings!", "Import/Export data"),
                List.of(VaadinIcon.PLAY.create(),
                        VaadinIcon.QUESTION.create(),
                        VaadinIcon.CODE.create(),
                        VaadinIcon.CALENDAR.create(),
                        VaadinIcon.COG.create(),
                        VaadinIcon.UPLOAD.create()),
                List.of(AbstractStartInterviewView.ROUTE_NAME,
                        AbstractInterviewQuestionsView.ROUTE_NAME,
                        AbstractCodingTaskView.ROUTE_NAME,
                        AbstractInterviewPlanView.ROUTE_NAME,
                        AbstractQuestionConfigView.ROUTE_NAME,
                        AbstractImportExportView.ROUTE_NAME)

        ));
    }
}
