package com.bervan.interviewapp.view;

import com.bervan.common.MenuNavigationComponent;
import com.vaadin.flow.component.icon.VaadinIcon;

public final class InterviewAppPageLayout extends MenuNavigationComponent {

    public InterviewAppPageLayout(String currentRouteName) {
        super(currentRouteName);
        addButtonIfVisible(menuButtonsRow, AbstractInterviewHomeView.ROUTE_NAME, "Home", VaadinIcon.HOME.create());
        addButtonIfVisible(menuButtonsRow, AbstractStartInterviewView.ROUTE_NAME, "Interview!", VaadinIcon.PLAY.create());
        addButtonIfVisible(menuButtonsRow, AbstractInterviewQuestionsView.ROUTE_NAME, "Interview Questions", VaadinIcon.QUESTION.create());
        addButtonIfVisible(menuButtonsRow, AbstractCodingTaskView.ROUTE_NAME, "Coding Tasks", VaadinIcon.CODE.create());
        addButtonIfVisible(menuButtonsRow, AbstractInterviewPlanView.ROUTE_NAME, "Interview Plan", VaadinIcon.CALENDAR.create());
        addButtonIfVisible(menuButtonsRow, AbstractInterviewSessionListView.ROUTE_NAME, "Sessions", VaadinIcon.RECORDS.create());
        addButtonIfVisible(menuButtonsRow, AbstractQuestionConfigView.ROUTE_NAME, "Question Config", VaadinIcon.COG.create());
        addButtonIfVisible(menuButtonsRow, AbstractImportExportView.ROUTE_NAME, "Import/Export", VaadinIcon.UPLOAD.create());
        add(menuButtonsRow);
    }
}
