package com.bervan.interviewapp.view;

import com.bervan.common.AbstractTableView;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.interviewapp.interviewquestions.QuestionTag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractInterviewQuestionsView extends AbstractTableView<Question> {
    public static final String ROUTE_NAME = "interview-app/interview-questions";


    public AbstractInterviewQuestionsView(@Autowired InterviewQuestionService questionService) {
        super(new InterviewAppPageLayout(ROUTE_NAME), questionService, "Interview Questions");
        renderCommonComponents();
    }

    @Override
    protected Grid<Question> getGrid() {
        Grid<Question> questionGrid = new Grid<>(Question.class, false);
        questionGrid.addColumn(new ComponentRenderer<>(question -> formatTextComponent(question.getName())))
                .setHeader("Name").setKey("name").setResizable(true).setSortable(true);
        questionGrid.addColumn(new ComponentRenderer<>(question -> formatTextComponent(question.getTags())))
                .setHeader("Tags").setKey("tags").setResizable(true).setSortable(true);
        questionGrid.addColumn(Question::getDifficulty).setHeader("Difficulty").setKey("difficulty").setResizable(true)
                .setSortable(true);
        questionGrid.addColumn(new ComponentRenderer<>(question -> formatTextComponent(question.getQuestionDetails())))
                .setHeader("Question Details").setKey("questionDetails").setResizable(true);
        questionGrid.addColumn(new ComponentRenderer<>(question -> formatTextComponent(question.getAnswerDetails())))
                .setHeader("Answer Details").setKey("answerDetails").setResizable(true);
        questionGrid.addColumn(Question::getMaxPoints).setHeader("Max Points").setKey("maxPoints").setResizable(true);
        questionGrid.getElement().getStyle().set("--lumo-size-m", 100 + "px");

        return questionGrid;
    }

    @Override
    protected void doOnColumnClick(ItemClickEvent<Question> event) {
        Dialog dialog = new Dialog();
        dialog.setWidth("80vw");

        VerticalLayout dialogLayout = new VerticalLayout();

        HorizontalLayout headerLayout = getDialogTopBarLayout(dialog);

        String clickedColumn = event.getColumn().getKey();
        TextArea field = new TextArea(clickedColumn);
        field.setWidth("100%");

        ComboBox<QuestionTag> tagsComboBox = new ComboBox<>("Tags");
        tagsComboBox.setItems(QuestionTag.values());
        tagsComboBox.setItemLabelGenerator(QuestionTag::getDisplayName);
        tagsComboBox.setWidth("100%");

        ComboBox<Integer> difficultyComboBox = new ComboBox<>("Difficulty");
        difficultyComboBox.setItems(1, 2, 3, 4, 5);
        difficultyComboBox.setWidth("100%");

        Question item = event.getItem();
        if ("tags".equals(clickedColumn)) {
            tagsComboBox.setValue(QuestionTag.valueOf(item.getTags().toUpperCase().replace("/", "_")));
        } else if ("difficulty".equals(clickedColumn)) {
            difficultyComboBox.setValue(item.getDifficulty());
        } else {
            switch (clickedColumn) {
                case "name":
                    field.setValue(item.getName());
                    break;
                case "questionDetails":
                    field.setValue(item.getQuestionDetails());
                    break;
                case "answerDetails":
                    field.setValue(item.getAnswerDetails());
                    break;
                case "maxPoints":
                    field.setValue(String.valueOf(item.getMaxPoints()));
                    break;
            }
        }

        Button saveButton = new Button("Save");
        saveButton.addClickListener(e -> {
            if ("tags".equals(clickedColumn)) {
                item.setTags(tagsComboBox.getValue().getDisplayName());
            } else if ("difficulty".equals(clickedColumn)) {
                item.setDifficulty(difficultyComboBox.getValue());
            } else {
                switch (clickedColumn) {
                    case "name":
                        item.setName(field.getValue());
                        break;
                    case "questionDetails":
                        item.setQuestionDetails(field.getValue());
                        break;
                    case "answerDetails":
                        item.setAnswerDetails(field.getValue());
                        break;
                    case "maxPoints":
                        item.setMaxPoints(Double.parseDouble(field.getValue()));
                        break;
                }
            }
            grid.getDataProvider().refreshItem(item);
            service.save(data.stream().toList());
            dialog.close();
        });

        if ("tags".equals(clickedColumn)) {
            dialogLayout.add(headerLayout, tagsComboBox, saveButton);
        } else if ("difficulty".equals(clickedColumn)) {
            dialogLayout.add(headerLayout, difficultyComboBox, saveButton);
        } else {
            dialogLayout.add(headerLayout, field, saveButton);
        }

        dialog.add(dialogLayout);

        dialog.open();
    }

    @Override
    protected void openAddDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("80vw");

        VerticalLayout dialogLayout = new VerticalLayout();

        HorizontalLayout headerLayout = getDialogTopBarLayout(dialog);

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
        dialog.add(dialogLayout);
        dialog.open();
    }
}