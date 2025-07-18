package com.bervan.interviewapp.view;

import com.bervan.common.AbstractBervanTableView;
import com.bervan.core.model.BervanLogger;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.interviewapp.interviewquestions.QuestionTag;
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


    public AbstractInterviewQuestionsView(@Autowired InterviewQuestionService questionService, BervanLogger log) {
        super(new InterviewAppPageLayout(ROUTE_NAME), questionService, log, Question.class);
        renderCommonComponents();
    }

    @Override
    protected Grid<Question> getGrid() {
        Grid<Question> grid = new Grid<>(Question.class, false);
        buildGridAutomatically(grid);
        return grid;
    }

//    @Override
//    protected void buildOnColumnClickDialogContent(Dialog dialog, VerticalLayout dialogLayout, HorizontalLayout headerLayout, String clickedColumn, Question item) {
//        TextArea field = new TextArea(clickedColumn);
//        field.setWidth("100%");
//
//        ComboBox<QuestionTag> tagsComboBox = new ComboBox<>("Tags");
//        tagsComboBox.setItems(QuestionTag.values());
//        tagsComboBox.setItemLabelGenerator(QuestionTag::getDisplayName);
//        tagsComboBox.setWidth("100%");
//
//        ComboBox<Integer> difficultyComboBox = new ComboBox<>("Difficulty");
//        difficultyComboBox.setItems(1, 2, 3, 4, 5);
//        difficultyComboBox.setWidth("100%");
//
//        if ("tags".equals(clickedColumn)) {
//            tagsComboBox.setValue(QuestionTag.valueOf(item.getTags().toUpperCase().replace("/", "_")));
//        } else if ("difficulty".equals(clickedColumn)) {
//            difficultyComboBox.setValue(item.getDifficulty());
//        } else {
//            switch (clickedColumn) {
//                case "name" -> field.setValue(item.getName());
//                case "questionDetails" -> field.setValue(item.getQuestionDetails());
//                case "answerDetails" -> field.setValue(item.getAnswerDetails());
//                case "maxPoints" -> field.setValue(String.valueOf(item.getMaxPoints()));
//            }
//        }
//
//        Button saveButton = new Button("Save");
//        saveButton.addClassName("option-button");
//
//        saveButton.addClickListener(e -> {
//            if ("tags".equals(clickedColumn)) {
//                item.setTags(tagsComboBox.getValue().getDisplayName());
//            } else if ("difficulty".equals(clickedColumn)) {
//                item.setDifficulty(difficultyComboBox.getValue());
//            } else {
//                switch (clickedColumn) {
//                    case "name" -> item.setName(field.getValue());
//                    case "questionDetails" -> item.setQuestionDetails(field.getValue());
//                    case "answerDetails" -> item.setAnswerDetails(field.getValue());
//                    case "maxPoints" -> item.setMaxPoints(Double.parseDouble(field.getValue()));
//                }
//            }
//            grid.getDataProvider().refreshItem(item);
//            service.save(data.stream().toList());
//            dialog.close();
//        });
//
//        if ("tags".equals(clickedColumn)) {
//            dialogLayout.add(headerLayout, tagsComboBox, saveButton);
//        } else if ("difficulty".equals(clickedColumn)) {
//            dialogLayout.add(headerLayout, difficultyComboBox, saveButton);
//        } else {
//            dialogLayout.add(headerLayout, field, saveButton);
//        }
//    }

    @Override
    protected void buildNewItemDialogContent(Dialog dialog, VerticalLayout dialogLayout, HorizontalLayout headerLayout) {
        TextField nameField = new TextField("Name");
        ComboBox<QuestionTag> tagsComboBox = new ComboBox<>("Tags");
        tagsComboBox.setItems(QuestionTag.values());
        tagsComboBox.setItemLabelGenerator(QuestionTag::getDisplayName);
        ComboBox<Integer> difficultyComboBox = new ComboBox<>("Difficulty");
        difficultyComboBox.setItems(1, 2, 3, 4, 5);
        TextArea questionDetailsField = new TextArea("Question Details");
        TextArea answerDetailsField = new TextArea("Answer Details");
        TextField maxPointsField = new TextField("Max Points");

        nameField.setWidth("100%");
        tagsComboBox.setWidth("100%");
        difficultyComboBox.setWidth("100%");
        questionDetailsField.setWidth("100%");
        answerDetailsField.setWidth("100%");
        maxPointsField.setWidth("100%");

        Button saveButton = new Button("Save");
        saveButton.addClassName("option-button");

        saveButton.addClickListener(e -> {
            String name = nameField.getValue();
            String tags = tagsComboBox.getValue().getDisplayName();
            int difficulty = difficultyComboBox.getValue();
            String questionDetails = questionDetailsField.getValue();
            String answerDetails = answerDetailsField.getValue();
            double maxPoints = Double.parseDouble(maxPointsField.getValue());

            Question newQuestion = new Question(name, tags, difficulty, questionDetails, answerDetails, maxPoints);
            data.add(newQuestion);
            grid.setItems(data); // Refresh the grid
            service.save(data.stream().toList());
            dialog.close();
        });
        dialogLayout.add(headerLayout, nameField, tagsComboBox, difficultyComboBox, questionDetailsField, answerDetailsField, maxPointsField, saveButton);
    }
}