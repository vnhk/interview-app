package com.bervan.interviewapp.view;

import com.bervan.common.AbstractPageView;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.interviewapp.questionconfig.QuestionConfig;
import com.bervan.interviewapp.questionconfig.QuestionConfigService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public abstract class AbstractStartInterviewView extends AbstractPageView {
    public static final String ROUTE_NAME = "interview-app/interview-process";
    public static final String L0 = "0-1";
    public static final String L1 = "1-2";
    public static final String L2 = "2-3";
    public static final String L3 = "3-4";
    public static final String L4 = "4-5";
    public static final String L5 = "5-6";
    public static final String L6 = "6+";
    public static String[] experienceOptions = {L0, L1, L2, L3, L4, L5, L6};
    private final InterviewAppPageLayout pageLayout;
    @Autowired
    private QuestionConfigService questionConfigService;
    @Autowired
    private InterviewQuestionService questionService;

    public AbstractStartInterviewView() {
        pageLayout = new InterviewAppPageLayout(ROUTE_NAME);
        add(pageLayout);

        ComboBox<String> comboBox = new ComboBox<>("Select candidate experience");
        comboBox.setItems(experienceOptions);

        Button generateButton = new Button("Generate");
        generateButton.addClassName("option-button");

        VerticalLayout interviewContent = new VerticalLayout();
        interviewContent.setVisible(false);

        generateButton.addClickListener(event -> {
            String selectedValue = comboBox.getValue();
            int selectedOptionNumber = 0;
            for (String experienceOption : experienceOptions) {
                if (experienceOption.equals(selectedValue)) {
                    break;
                } else {
                    selectedOptionNumber++;
                }
            }
            if (selectedValue != null) {
                interviewContent.setVisible(true);
                interviewContent.removeAll();
                Map<Integer, Integer> amountOfLvlBasedQuestions = getAmountOfLvlBasedQuestions("L" + selectedOptionNumber);
                Integer amountOfSpringQuestions = getAmountOfSpringSecurityQuestions("L" + selectedOptionNumber);
                Random random = new Random();

                for (Map.Entry<Integer, Integer> integerIntegerEntry : amountOfLvlBasedQuestions.entrySet()) {
                    List<Question> questionsByDifficulty = this.questionService.findByDifficultyNotSpringSecurity(integerIntegerEntry.getKey());
                    Integer amountOfQuestions = integerIntegerEntry.getValue();
                    Set<Integer> randomIndexHolder = new HashSet<>();
                    while (randomIndexHolder.size() < amountOfQuestions) {
                        randomIndexHolder.add(random.nextInt(questionsByDifficulty.size()) + 1);
                    }

                    for (Integer index : randomIndexHolder) {
                        add(buildQuestionView(questionsByDifficulty.get(index)));
                    }
                }
            } else {
                Notification.show("Please select a value from the dropdown.");
            }
        });

        add(comboBox, generateButton, interviewContent);
    }

    private HorizontalLayout buildQuestionView(Question question) {
        HorizontalLayout result = new HorizontalLayout();
        Text t1 = new Text(question.getName());
        Text t2 = new Text(question.getQuestionDetails());
        Text t3 = new Text(question.getAnswerDetails());

        result.add(t1, t2, t3);

        return result;
    }

    private Map<Integer, Integer> getAmountOfLvlBasedQuestions(String selectedLvl) {
        Map<Integer, Integer> result = new HashMap<>();
        Optional<QuestionConfig> questionConfig =
                questionConfigService.loadByName(selectedLvl);

        if (questionConfig.isPresent()) {
            QuestionConfig config = questionConfig.get();
            result.put(1, config.getDifficulty1Amount());
            result.put(2, config.getDifficulty2Amount());
            result.put(3, config.getDifficulty3Amount());
            result.put(4, config.getDifficulty4Amount());
            result.put(5, config.getDifficulty5Amount());
        } else {
            throw new RuntimeException("No question config!");
        }

        return result;
    }

    private Integer getAmountOfSpringSecurityQuestions(String selectedLvl) {
        Optional<QuestionConfig> questionConfig =
                questionConfigService.loadByName(selectedLvl);

        if (questionConfig.isPresent()) {
            QuestionConfig config = questionConfig.get();
            return config.getSpringSecurityAmount();
        } else {
            throw new RuntimeException("No question config!");
        }
    }
}
