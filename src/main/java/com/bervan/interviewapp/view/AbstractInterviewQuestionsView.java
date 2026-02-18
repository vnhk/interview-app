package com.bervan.interviewapp.view;

import com.bervan.common.config.BervanViewConfig;
import com.bervan.common.view.AbstractBervanTableView;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public abstract class AbstractInterviewQuestionsView extends AbstractBervanTableView<UUID, Question> {
    public static final String ROUTE_NAME = "interview-app/interview-questions";


    public AbstractInterviewQuestionsView(@Autowired InterviewQuestionService questionService, BervanViewConfig bervanViewConfig) {
        super(new InterviewAppPageLayout(ROUTE_NAME), questionService, bervanViewConfig, Question.class);
        renderCommonComponents();
    }

    @Override
    protected Grid<Question> getGrid() {
        Grid<Question> grid = new Grid<>(Question.class, false);
        buildGridAutomatically(grid);
        return grid;
    }

    @Override
    protected void buildNewItemDialogContent(Dialog dialog, VerticalLayout dialogLayout, HorizontalLayout headerLayout) {
        TextField nameField = new TextField("Name");
        TextField tagsField = new TextField("Tags (comma-separated)");
        tagsField.setPlaceholder("e.g. Java, Spring, Security");
        ComboBox<Integer> difficultyComboBox = new ComboBox<>("Difficulty");
        difficultyComboBox.setItems(1, 2, 3, 4, 5);
        TextArea questionDetailsField = new TextArea("Question Details");
        TextArea answerDetailsField = new TextArea("Answer Details");
        TextField maxPointsField = new TextField("Max Points");

        nameField.setWidth("100%");
        tagsField.setWidth("100%");
        difficultyComboBox.setWidth("100%");
        questionDetailsField.setWidth("100%");
        answerDetailsField.setWidth("100%");
        maxPointsField.setWidth("100%");

        Button saveButton = new Button("Save");
        saveButton.addClassName("option-button");

        saveButton.addClickListener(e -> {
            String name = nameField.getValue();
            String tags = tagsField.getValue();
            int difficulty = difficultyComboBox.getValue();
            String questionDetails = questionDetailsField.getValue();
            String answerDetails = answerDetailsField.getValue();
            double maxPoints = Double.parseDouble(maxPointsField.getValue());

            Question newQuestion = new Question(name, tags, difficulty, questionDetails, answerDetails, maxPoints);
            data.add(newQuestion);
            grid.setItems(data);
            service.save(data.stream().toList());
            dialog.close();
        });
        dialogLayout.add(headerLayout, nameField, tagsField, difficultyComboBox, questionDetailsField, answerDetailsField, maxPointsField, saveButton);
    }
}
