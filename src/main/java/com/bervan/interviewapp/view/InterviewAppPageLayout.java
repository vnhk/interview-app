package com.bervan.interviewapp.view;

import com.bervan.common.MenuNavigationComponent;

public final class InterviewAppPageLayout extends MenuNavigationComponent {

    public InterviewAppPageLayout(String currentRouteName) {
        super(currentRouteName);

        addButtonIfVisible(menuButtonsRow, AbstractInterviewHomeView.ROUTE_NAME, "Home");
        addButtonIfVisible(menuButtonsRow, AbstractStartInterviewView.ROUTE_NAME, "Interview!");
        addButtonIfVisible(menuButtonsRow, AbstractInterviewQuestionsView.ROUTE_NAME, "Interview Questions");
        addButtonIfVisible(menuButtonsRow, AbstractCodingTaskView.ROUTE_NAME, "Coding Tasks");
        addButtonIfVisible(menuButtonsRow, AbstractInterviewPlanView.ROUTE_NAME, "Interview Plan");
        addButtonIfVisible(menuButtonsRow, AbstractQuestionConfigView.ROUTE_NAME, "Question Config");
        addButtonIfVisible(menuButtonsRow, AbstractImportExportView.ROUTE_NAME, "Import/Export");

        add(menuButtonsRow);
    }
}
