package com.bervan.interviewapp.view;

import com.bervan.common.config.BervanViewConfig;
import com.bervan.common.view.AbstractBervanTableView;
import com.bervan.interviewapp.questionconfig.QuestionConfig;
import com.bervan.interviewapp.questionconfig.QuestionConfigService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class AbstractQuestionConfigView extends AbstractBervanTableView<UUID, QuestionConfig> {
    public static final String ROUTE_NAME = "interview-app/question-config";


    public AbstractQuestionConfigView(@Autowired QuestionConfigService service, BervanViewConfig bervanViewConfig) {
        super(new InterviewAppPageLayout(ROUTE_NAME), service, bervanViewConfig, QuestionConfig.class);
        renderCommonComponents();
    }

    @Override
    protected Grid<QuestionConfig> getGrid() {
        Grid<QuestionConfig> grid = new Grid<>(QuestionConfig.class, false);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(questionConfig.getTableFilterableColumnValue())))
                .setHeader("Name").setKey("name").setResizable(true).setSortable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(percentStr(questionConfig.getDifficulty1Percent()))))
                .setHeader("Level 1 %").setKey("difficulty1Percent").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(percentStr(questionConfig.getDifficulty2Percent()))))
                .setHeader("Level 2 %").setKey("difficulty2Percent").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(percentStr(questionConfig.getDifficulty3Percent()))))
                .setHeader("Level 3 %").setKey("difficulty3Percent").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(percentStr(questionConfig.getDifficulty4Percent()))))
                .setHeader("Level 4 %").setKey("difficulty4Percent").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(percentStr(questionConfig.getDifficulty5Percent()))))
                .setHeader("Level 5 %").setKey("difficulty5Percent").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(
                        questionConfig.getCodingTasksAmount() != null ? String.valueOf(questionConfig.getCodingTasksAmount()) : "0")))
                .setHeader("Coding Tasks").setKey("codingTasksAmount").setResizable(true);
        grid.getElement().getStyle().set("--lumo-size-m", 100 + "px");

        return grid;
    }

    private String percentStr(Integer value) {
        return value != null ? value + "%" : "0%";
    }

    @Override
    protected void buildOnColumnClickDialogContent(Dialog dialog, VerticalLayout dialogLayout, HorizontalLayout headerLayout, String clickedField, QuestionConfig item) {
        TextFieldBase field;

        switch (clickedField) {
            case "name" -> {
                field = new TextField("Name");
                field.setWidth("100%");
                field.setValue(item.getTableFilterableColumnValue());
            }
            case "difficulty1Percent" -> {
                field = createPercentField("Level 1 %");
                field.setValue(item.getDifficulty1Percent());
            }
            case "difficulty2Percent" -> {
                field = createPercentField("Level 2 %");
                field.setValue(item.getDifficulty2Percent());
            }
            case "difficulty3Percent" -> {
                field = createPercentField("Level 3 %");
                field.setValue(item.getDifficulty3Percent());
            }
            case "difficulty4Percent" -> {
                field = createPercentField("Level 4 %");
                field.setValue(item.getDifficulty4Percent());
            }
            case "difficulty5Percent" -> {
                field = createPercentField("Level 5 %");
                field.setValue(item.getDifficulty5Percent());
            }
            case "codingTasksAmount" -> {
                IntegerField integerField = new IntegerField("Coding Tasks");
                integerField.setMin(0);
                integerField.setMax(20);
                field = integerField;
                field.setWidth("100%");
                field.setValue(item.getCodingTasksAmount());
            }
            default -> throw new RuntimeException("Invalid column!");
        }

        Button saveButton = new Button("Save");
        saveButton.addClassName("option-button");

        saveButton.addClickListener(e -> {

            switch (clickedField) {
                case "name" -> item.setName((String) field.getValue());
                case "difficulty1Percent" -> item.setDifficulty1Percent((Integer) field.getValue());
                case "difficulty2Percent" -> item.setDifficulty2Percent((Integer) field.getValue());
                case "difficulty3Percent" -> item.setDifficulty3Percent((Integer) field.getValue());
                case "difficulty4Percent" -> item.setDifficulty4Percent((Integer) field.getValue());
                case "difficulty5Percent" -> item.setDifficulty5Percent((Integer) field.getValue());
                case "codingTasksAmount" -> item.setCodingTasksAmount((Integer) field.getValue());
            }

            grid.getDataProvider().refreshItem(item);
            service.save(item);
            dialog.close();
        });


        dialogLayout.add(headerLayout, field, saveButton);
        dialog.add(dialogLayout);
        dialog.open();
    }

    @Override
    protected void buildNewItemDialogContent(Dialog dialog, VerticalLayout dialogLayout, HorizontalLayout headerLayout) {
        TextField nameField = new TextField("Name");
        nameField.setWidth("75%");

        IntegerField difficulty1Percent = createPercentField("Level 1 %");
        IntegerField difficulty2Percent = createPercentField("Level 2 %");
        IntegerField difficulty3Percent = createPercentField("Level 3 %");
        IntegerField difficulty4Percent = createPercentField("Level 4 %");
        IntegerField difficulty5Percent = createPercentField("Level 5 %");

        IntegerField codingTasksAmount = new IntegerField("Coding Tasks");
        codingTasksAmount.setMin(0);
        codingTasksAmount.setMax(20);
        codingTasksAmount.setWidth("25%");

        Span sumInfo = new Span("Percentages should sum to 100%");
        sumInfo.getStyle().set("color", "var(--bervan-text-secondary, #94a3b8)").set("font-size", "0.85rem");

        Button saveButton = new Button("Save");
        saveButton.addClassName("option-button");

        saveButton.addClickListener(e -> {
            String name = nameField.getValue();
            Integer p1 = difficulty1Percent.getValue() != null ? difficulty1Percent.getValue() : 0;
            Integer p2 = difficulty2Percent.getValue() != null ? difficulty2Percent.getValue() : 0;
            Integer p3 = difficulty3Percent.getValue() != null ? difficulty3Percent.getValue() : 0;
            Integer p4 = difficulty4Percent.getValue() != null ? difficulty4Percent.getValue() : 0;
            Integer p5 = difficulty5Percent.getValue() != null ? difficulty5Percent.getValue() : 0;

            int sum = p1 + p2 + p3 + p4 + p5;
            if (sum != 100) {
                showWarningNotification("Percentages must sum to 100% (currently " + sum + "%)");
                return;
            }

            QuestionConfig newQuestionConfig = new QuestionConfig();
            newQuestionConfig.setName(name);
            newQuestionConfig.setDifficulty1Percent(p1);
            newQuestionConfig.setDifficulty2Percent(p2);
            newQuestionConfig.setDifficulty3Percent(p3);
            newQuestionConfig.setDifficulty4Percent(p4);
            newQuestionConfig.setDifficulty5Percent(p5);
            newQuestionConfig.setCodingTasksAmount(codingTasksAmount.getValue() != null ? codingTasksAmount.getValue() : 0);
            data.add(newQuestionConfig);
            grid.setItems(data);
            service.save(data.stream().toList());
            dialog.close();
        });
        dialogLayout.add(headerLayout, nameField, difficulty1Percent, difficulty2Percent, difficulty3Percent, difficulty4Percent, difficulty5Percent, codingTasksAmount, sumInfo, saveButton);
    }

    private IntegerField createPercentField(String label) {
        IntegerField field = new IntegerField(label);
        field.setMin(0);
        field.setMax(100);
        field.setSuffixComponent(new Span("%"));
        field.setWidth("25%");
        return field;
    }
}
