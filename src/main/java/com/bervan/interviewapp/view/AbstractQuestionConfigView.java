package com.bervan.interviewapp.view;

import com.bervan.common.AbstractTableView;
import com.bervan.interviewapp.questionconfig.QuestionConfig;
import com.bervan.interviewapp.questionconfig.QuestionConfigService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractQuestionConfigView extends AbstractTableView<QuestionConfig> {
    public static final String ROUTE_NAME = "interview-app/question-config";


    public AbstractQuestionConfigView(@Autowired QuestionConfigService service) {
        super(new InterviewAppPageLayout(ROUTE_NAME), service, "Coding Tasks");
    }

    @Override
    protected Grid<QuestionConfig> getGrid() {
        Grid<QuestionConfig> grid = new Grid<>(QuestionConfig.class, false);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(questionConfig.getName())))
                .setHeader("Name").setKey("name").setResizable(true).setSortable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(String.valueOf(questionConfig.getDifficulty1Amount()))))
                .setHeader("Level 1 Questions").setKey("difficulty1Amount").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(String.valueOf(questionConfig.getDifficulty2Amount()))))
                .setHeader("Level 2 Questions").setKey("difficulty2Amount").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(String.valueOf(questionConfig.getDifficulty3Amount()))))
                .setHeader("Level 3 Questions").setKey("difficulty3Amount").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(String.valueOf(questionConfig.getDifficulty4Amount()))))
                .setHeader("Level 4 Questions").setKey("difficulty4Amount").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(String.valueOf(questionConfig.getDifficulty5Amount()))))
                .setHeader("Level 5 Questions").setKey("difficulty5Amount").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(questionConfig -> formatTextComponent(String.valueOf(questionConfig.getSpringSecurityAmount()))))
                .setHeader("Spring Security Questions").setKey("springSecurityAmount").setResizable(true);
        grid.getElement().getStyle().set("--lumo-size-m", 100 + "px");

        return grid;
    }

    @Override
    protected void openClickOnColumnDialog(ItemClickEvent<QuestionConfig> event) {
        Dialog dialog = new Dialog();
        dialog.setWidth("80vw");

        VerticalLayout dialogLayout = new VerticalLayout();

        HorizontalLayout headerLayout = getDialogTopBarLayout(dialog);

        String clickedColumn = event.getColumn().getKey();
        TextFieldBase field;

        QuestionConfig item = event.getItem();

        switch (clickedColumn) {
            case "name" -> {
                field = new IntegerField("Name");
                field.setWidth("100%");
                field.setValue(item.getName());
            }
            case "difficulty1Amount" -> {
                IntegerField integerField = new IntegerField("Level 1 Questions");
                integerField.setMax(20);
                field = integerField;
                field.setWidth("100%");
                field.setValue(item.getDifficulty1Amount());
            }
            case "difficulty2Amount" -> {
                IntegerField integerField = new IntegerField("Level 2 Questions");
                integerField.setMax(20);
                field = integerField;
                field.setWidth("100%");
                field.setValue(item.getDifficulty2Amount());
            }
            case "difficulty3Amount" -> {
                IntegerField integerField = new IntegerField("Level 3 Questions");
                integerField.setMax(20);
                field = integerField;
                field.setWidth("100%");
                field.setValue(item.getDifficulty3Amount());
            }
            case "difficulty4Amount" -> {
                IntegerField integerField = new IntegerField("Level 4 Questions");
                integerField.setMax(20);
                field = integerField;
                field.setWidth("100%");
                field.setValue(item.getDifficulty4Amount());
            }
            case "difficulty5Amount" -> {
                IntegerField integerField = new IntegerField("Level 5 Questions");
                integerField.setMax(20);
                field = integerField;
                field.setWidth("100%");
                field.setValue(item.getDifficulty5Amount());
            }
            case "springSecurityAmount" -> {
                IntegerField integerField = new IntegerField("Spring Security Questions");
                integerField.setMax(20);
                field = integerField;
                field.setWidth("100%");
                field.setValue(item.getSpringSecurityAmount());
            }
            default -> throw new RuntimeException("Invalid column!");
        }


        Button saveButton = new Button("Save");
        saveButton.addClickListener(e -> {

            switch (clickedColumn) {
                case "name" -> item.setName((String) field.getValue());
                case "difficulty1Amount" -> item.setDifficulty1Amount((Integer) field.getValue());
                case "difficulty2Amount" -> item.setDifficulty2Amount((Integer) field.getValue());
                case "difficulty3Amount" -> item.setDifficulty3Amount((Integer) field.getValue());
                case "difficulty4Amount" -> item.setDifficulty4Amount((Integer) field.getValue());
                case "difficulty5Amount" -> item.setDifficulty5Amount((Integer) field.getValue());
                case "springSecurityAmount" -> item.setSpringSecurityAmount((Integer) field.getValue());
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
    protected void openAddDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("80vw");

        VerticalLayout dialogLayout = new VerticalLayout();

        HorizontalLayout headerLayout = getDialogTopBarLayout(dialog);

        TextField nameField = new TextField("Name");
        nameField.setWidth("75%");

        IntegerField difficulty1Amount = new IntegerField("Level 1 Questions");
        difficulty1Amount.setMax(20);
        difficulty1Amount.setWidth("25%");

        IntegerField difficulty2Amount = new IntegerField("Level 2 Questions");
        difficulty2Amount.setMax(20);
        difficulty2Amount.setWidth("25%");

        IntegerField difficulty3Amount = new IntegerField("Level 3 Questions");
        difficulty3Amount.setMax(20);
        difficulty3Amount.setWidth("25%");

        IntegerField difficulty4Amount = new IntegerField("Level 4 Questions");
        difficulty4Amount.setMax(20);
        difficulty4Amount.setWidth("25%");

        IntegerField difficulty5Amount = new IntegerField("Level 5 Questions");
        difficulty5Amount.setMax(20);
        difficulty5Amount.setWidth("25%");

        IntegerField springSecurityAmount = new IntegerField("Spring Security Questions");
        springSecurityAmount.setMax(20);
        springSecurityAmount.setWidth("25%");

        Button saveButton = new Button("Save");
        saveButton.addClickListener(e -> {
            String name = nameField.getValue();
            Integer a1 = difficulty1Amount.getValue();
            Integer a2 = difficulty2Amount.getValue();
            Integer a3 = difficulty3Amount.getValue();
            Integer a4 = difficulty4Amount.getValue();
            Integer a5 = difficulty5Amount.getValue();
            Integer s = springSecurityAmount.getValue();

            QuestionConfig newQuestionConfig = new QuestionConfig();
            newQuestionConfig.setName(name);
            newQuestionConfig.setDifficulty1Amount(a1);
            newQuestionConfig.setDifficulty2Amount(a2);
            newQuestionConfig.setDifficulty3Amount(a3);
            newQuestionConfig.setDifficulty4Amount(a4);
            newQuestionConfig.setDifficulty5Amount(a5);
            newQuestionConfig.setSpringSecurityAmount(s);
            data.add(newQuestionConfig);
            grid.setItems(data); // Refresh the grid
            service.save(data);
            dialog.close();
        });
        dialogLayout.add(headerLayout, nameField, difficulty1Amount, difficulty2Amount, difficulty3Amount, difficulty4Amount, difficulty5Amount, saveButton);
        dialog.add(dialogLayout);
        dialog.open();
    }
}