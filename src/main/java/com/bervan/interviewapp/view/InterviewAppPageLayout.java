package com.bervan.interviewapp.view;

import com.bervan.common.AbstractPageLayout;
import com.vaadin.flow.component.html.Hr;

public final class InterviewAppPageLayout extends AbstractPageLayout {

    public InterviewAppPageLayout(String currentRouteName) {
        super(currentRouteName);

        addButton(menuButtonsRow, AbstractInterviewHomeView.ROUTE_NAME, "Home");
        addButton(menuButtonsRow, AbstractStartInterviewView.ROUTE_NAME, "Interview!");
        addButton(menuButtonsRow, AbstractInterviewQuestionsView.ROUTE_NAME, "Interview Questions");
        addButton(menuButtonsRow, AbstractCodingTaskView.ROUTE_NAME, "Coding Tasks");
        addButton(menuButtonsRow, AbstractInterviewPlanView.ROUTE_NAME, "Interview Plan");
        addButton(menuButtonsRow, AbstractQuestionConfigView.ROUTE_NAME, "Question Config");
        addButton(menuButtonsRow, AbstractImportExportView.ROUTE_NAME, "Import/Export");

        add(menuButtonsRow);
        add(new Hr());

    }
}
