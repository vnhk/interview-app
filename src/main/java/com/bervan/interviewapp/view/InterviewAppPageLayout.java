package com.bervan.interviewapp.view;

import com.bervan.common.AbstractPageLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.HashMap;
import java.util.Map;

public final class InterviewAppPageLayout extends AbstractPageLayout {
    private final Map<String, Button> buttons = new HashMap<>();

    public InterviewAppPageLayout(String currentRouteName) {
        super(currentRouteName);

        HorizontalLayout menuRow = new HorizontalLayout();
        addButton(menuRow, AbstractInterviewHomeView.ROUTE_NAME, "Home");
        addButton(menuRow, AbstractStartInterviewView.ROUTE_NAME, "Interview!");
        addButton(menuRow, AbstractInterviewQuestionsView.ROUTE_NAME, "Interview Questions");
        addButton(menuRow, AbstractCodingTaskView.ROUTE_NAME, "Coding Tasks");
        addButton(menuRow, AbstractInterviewPlanView.ROUTE_NAME, "Interview Plan");
        addButton(menuRow, AbstractQuestionConfigView.ROUTE_NAME, "Question Config");
        addButton(menuRow, AbstractImportExportView.ROUTE_NAME, "Import/Export");

        add(menuRow);
        add(new Hr());

    }

    public void notification(String message) {
        Notification.show(message);
    }

    public void addButton(HorizontalLayout menuRow, String routeName, String buttonText) {
        Button button = new Button(buttonText);
        button.addClickListener(buttonClickEvent ->
                button.getUI().ifPresent(ui -> ui.navigate(routeName)));

        buttons.put(routeName, button);

        if (routeName.equals(CURRENT_ROUTE_NAME)) {
            button.getStyle().set("background-color", "blue");
            button.getStyle().set("color", "white");
        }

        menuRow.add(button);
    }

}
