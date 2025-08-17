package com.bervan.interviewapp.view;

import com.bervan.common.MenuNavigationComponent;
import com.vaadin.flow.component.icon.VaadinIcon;

public final class InterviewAppPageLayout extends MenuNavigationComponent {

    public InterviewAppPageLayout(String currentRouteName) {
        super(currentRouteName);

        addButtonIfVisible(menuButtonsRow, AbstractInterviewHomeView.ROUTE_NAME, "Home", VaadinIcon.HOME.create());
        addButtonIfVisible(menuButtonsRow, AbstractStartInterviewView.ROUTE_NAME, "Interview!", VaadinIcon.HOME.create());
        addButtonIfVisible(menuButtonsRow, AbstractInterviewQuestionsView.ROUTE_NAME, "Interview Questions", VaadinIcon.HOME.create());
        addButtonIfVisible(menuButtonsRow, AbstractCodingTaskView.ROUTE_NAME, "Coding Tasks", VaadinIcon.HOME.create());
        addButtonIfVisible(menuButtonsRow, AbstractInterviewPlanView.ROUTE_NAME, "Interview Plan", VaadinIcon.HOME.create());
        addButtonIfVisible(menuButtonsRow, AbstractQuestionConfigView.ROUTE_NAME, "Question Config", VaadinIcon.HOME.create());
        addButtonIfVisible(menuButtonsRow, AbstractImportExportView.ROUTE_NAME, "Import/Export", VaadinIcon.HOME.create());

        add(menuButtonsRow);
    }
}
