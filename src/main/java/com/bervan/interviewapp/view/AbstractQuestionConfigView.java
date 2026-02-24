package com.bervan.interviewapp.view;

import com.bervan.common.config.BervanViewConfig;
import com.bervan.common.view.AbstractBervanTableView;
import com.bervan.interviewapp.questionconfig.QuestionConfig;
import com.bervan.interviewapp.questionconfig.QuestionConfigService;
import com.bervan.logging.JsonLogger;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class AbstractQuestionConfigView extends AbstractBervanTableView<UUID, QuestionConfig> {
    public static final String ROUTE_NAME = "interview-app/question-config";
    private static final JsonLogger log = JsonLogger.getLogger(AbstractQuestionConfigView.class, "interview-app");

    public AbstractQuestionConfigView(@Autowired QuestionConfigService service, BervanViewConfig bervanViewConfig) {
        super(new InterviewAppPageLayout(ROUTE_NAME), service, bervanViewConfig, QuestionConfig.class);
        renderCommonComponents();
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
            try {
                service.save(newQuestionConfig);
                data.add(newQuestionConfig);
                grid.setItems(data);
                dialog.close();
            } catch (Exception e2) {
                log.error("Could not save configuration!", e2);
                showErrorNotification("Could not save configuration!");
            }
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
