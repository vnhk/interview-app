package com.bervan.interviewapp.view;

import com.bervan.common.AbstractTableView;
import com.bervan.interviewapp.codingtask.CodingTask;
import com.bervan.interviewapp.codingtask.CodingTaskService;
import com.vaadin.flow.component.button.Button;
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

import java.time.LocalDateTime;

@Component
public abstract class AbstractCodingTaskView extends AbstractTableView<CodingTask> {
    public static final String ROUTE_NAME = "interview-app/coding-tasks";


    public AbstractCodingTaskView(@Autowired CodingTaskService service) {
        super(new InterviewAppPageLayout(ROUTE_NAME), service, "Coding Tasks");
    }

    @Override
    protected Grid<CodingTask> getGrid() {
        Grid<CodingTask> grid = new Grid<>(CodingTask.class, false);
        grid.addColumn(new ComponentRenderer<>(CodingTask -> formatTextComponent(CodingTask.getName())))
                .setHeader("Name").setKey("name").setResizable(true).setSortable(true);
        grid.addColumn(new ComponentRenderer<>(CodingTask -> formatTextComponent(CodingTask.getInitialCode())))
                .setHeader("Initial Code").setKey("initialCode").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(CodingTask -> formatTextComponent(CodingTask.getExampleCode())))
                .setHeader("Example Code").setKey("exampleCode").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(CodingTask -> formatTextComponent(CodingTask.getExampleCodeDetails())))
                .setHeader("Example Code Details").setKey("exampleCodeDetails").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(CodingTask -> formatTextComponent(CodingTask.getQuestions())))
                .setHeader("Questions").setKey("questions").setResizable(true);
        grid.getElement().getStyle().set("--lumo-size-m", 100 + "px");


        return grid;
    }

    @Override
    protected void openEditDialog(ItemClickEvent<CodingTask> event) {
        Dialog dialog = new Dialog();
        dialog.setWidth("80vw");

        VerticalLayout dialogLayout = new VerticalLayout();

        HorizontalLayout headerLayout = getDialogTopBarLayout(dialog);

        String clickedColumn = event.getColumn().getKey();
        TextArea field = new TextArea(clickedColumn);
        field.setWidth("100%");

        CodingTask item = event.getItem();

        switch (clickedColumn) {
            case "name":
                field.setValue(item.getName());
                break;
            case "initialCode":
                field.setValue(item.getInitialCode());
                break;
            case "exampleCode":
                field.setValue(item.getExampleCode());
                break;
            case "exampleCodeDetails":
                field.setValue(item.getExampleCodeDetails());
                break;
            case "questions":
                field.setValue(item.getQuestions());
                break;
        }


        Button saveButton = new Button("Save");
        saveButton.addClickListener(e -> {

            switch (clickedColumn) {
                case "name":
                    item.setName(field.getValue());
                    break;
                case "initialCode":
                    item.setInitialCode(field.getValue());
                    break;
                case "exampleCode":
                    item.setExampleCode(field.getValue());
                    break;
                case "exampleCodeDetails":
                    item.setExampleCodeDetails(field.getValue());
                    break;
                case "questions":
                    item.setQuestions(field.getValue());
                    break;
            }

            grid.getDataProvider().refreshItem(item);
            service.save(data);
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
        TextArea initialCodeField = new TextArea("Initial Code");
        TextArea exampleCodeField = new TextArea("Example Code");
        TextArea exampleCodeDetailsField = new TextArea("Example Code Details");
        TextArea questionsField = new TextArea("Questions");

        nameField.setWidth("100%");
        initialCodeField.setWidth("100%");
        exampleCodeField.setWidth("100%");
        exampleCodeDetailsField.setWidth("100%");
        questionsField.setWidth("100%");

        Button saveButton = new Button("Save");
        saveButton.addClickListener(e -> {
            String name = nameField.getValue();
            String initialCode = initialCodeField.getValue();
            String exampleCode = exampleCodeField.getValue();
            String exampleCodeDetails = exampleCodeDetailsField.getValue();
            String questions = questionsField.getValue();

            CodingTask newCodingTask = new CodingTask(name, initialCode, exampleCode, exampleCodeDetails, questions, LocalDateTime.now());
            data.add(newCodingTask);
            grid.setItems(data); // Refresh the grid
            service.save(data);
            dialog.close();
        });
        dialogLayout.add(headerLayout, nameField, initialCodeField, exampleCodeField, exampleCodeDetailsField, questionsField, saveButton);
        dialog.add(dialogLayout);
        dialog.open();
    }
}