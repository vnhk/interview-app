package com.bervan.interviewapp.view;

import com.bervan.common.view.AbstractPageView;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.interviewapp.questionconfig.QuestionConfig;
import com.bervan.interviewapp.questionconfig.QuestionConfigService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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

        ComboBox<String> comboBox = new ComboBox<>("Select candidate experience (years)");
        comboBox.setItems(experienceOptions);
        comboBox.setWidth("300px");

        Button generateButton = new Button("Generate Interview", new Icon(VaadinIcon.PLAY));
        generateButton.addClassName("option-button");

        HorizontalLayout controlsRow = new HorizontalLayout(comboBox, generateButton);
        controlsRow.setAlignItems(FlexComponent.Alignment.END);

        VerticalLayout interviewContent = new VerticalLayout();
        interviewContent.setWidthFull();
        interviewContent.setPadding(false);
        interviewContent.setVisible(false);

        generateButton.addClickListener(event -> {
            String selectedValue = comboBox.getValue();
            if (selectedValue == null) {
                showWarningNotification("Please select a value from the dropdown.");
                return;
            }

            int selectedOptionNumber = 0;
            for (String experienceOption : experienceOptions) {
                if (experienceOption.equals(selectedValue)) {
                    break;
                } else {
                    selectedOptionNumber++;
                }
            }

            interviewContent.setVisible(true);
            interviewContent.removeAll();

            String configName = "L" + selectedOptionNumber;
            Map<Integer, Integer> amountOfLvlBasedQuestions = getAmountOfLvlBasedQuestions(configName, selectedOptionNumber);
            Integer amountOfSpringQuestions = getAmountOfSpringSecurityQuestions(configName, selectedOptionNumber);

            H3 header = new H3("Interview Plan — Experience: " + selectedValue + " years");
            interviewContent.add(header);

            int questionNumber = 1;
            Random random = new Random();

            // Questions grouped by difficulty
            for (Map.Entry<Integer, Integer> entry : new TreeMap<>(amountOfLvlBasedQuestions).entrySet()) {
                int difficulty = entry.getKey();
                int amount = entry.getValue();
                if (amount <= 0) continue;

                List<Question> pool = questionService.findByDifficultyNotSpringSecurity(difficulty);
                if (pool.isEmpty()) continue;

                List<Question> selected = pickRandom(pool, amount, random);

                H4 sectionTitle = new H4("Difficulty " + difficulty + " (" + getDifficultyLabel(difficulty) + ")");
                sectionTitle.getStyle()
                        .set("margin-top", "16px")
                        .set("margin-bottom", "4px")
                        .set("color", getDifficultyColor(difficulty));
                interviewContent.add(sectionTitle);

                for (Question q : selected) {
                    interviewContent.add(buildQuestionCard(questionNumber++, q));
                }
            }

            // Spring Security questions
            if (amountOfSpringQuestions != null && amountOfSpringQuestions > 0) {
                List<Question> securityPool = questionService.findByDifficultySpringSecurity();
                if (!securityPool.isEmpty()) {
                    H4 secTitle = new H4("Spring Security");
                    secTitle.getStyle()
                            .set("margin-top", "16px")
                            .set("margin-bottom", "4px")
                            .set("color", "#a855f7");
                    interviewContent.add(secTitle);

                    List<Question> selected = pickRandom(securityPool, amountOfSpringQuestions, random);
                    for (Question q : selected) {
                        interviewContent.add(buildQuestionCard(questionNumber++, q));
                    }
                }
            }

            // Summary
            Div summary = new Div();
            summary.getStyle()
                    .set("margin-top", "24px")
                    .set("padding", "12px 16px")
                    .set("border-radius", "8px")
                    .set("background", "rgba(99, 102, 241, 0.1)");
            summary.add(new Span("Total questions: " + (questionNumber - 1)));
            interviewContent.add(summary);
        });

        add(controlsRow, interviewContent);
    }

    private List<Question> pickRandom(List<Question> pool, int amount, Random random) {
        if (amount >= pool.size()) {
            return new ArrayList<>(pool);
        }
        List<Question> shuffled = new ArrayList<>(pool);
        Collections.shuffle(shuffled, random);
        return shuffled.subList(0, amount);
    }

    private com.vaadin.flow.component.Component buildQuestionCard(int number, Question question) {
        Div card = new Div();
        card.getStyle()
                .set("width", "100%")
                .set("padding", "12px 16px")
                .set("margin-bottom", "8px")
                .set("border-radius", "8px")
                .set("border", "1px solid var(--bervan-border-color, #334155)")
                .set("background", "var(--bervan-surface-hover, rgba(255,255,255,0.03))");

        // Header row: number + name + difficulty badge + max points
        HorizontalLayout headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(FlexComponent.Alignment.CENTER);

        Span numberBadge = new Span("#" + number);
        numberBadge.getStyle()
                .set("font-weight", "700")
                .set("min-width", "36px")
                .set("color", "var(--bervan-text-secondary, #94a3b8)");

        Span name = new Span(question.getName() != null ? question.getName() : "—");
        name.getStyle()
                .set("font-weight", "600")
                .set("font-size", "1.05rem")
                .set("flex-grow", "1");

        Span diffBadge = new Span("Lvl " + question.getDifficulty());
        diffBadge.getStyle()
                .set("padding", "2px 10px")
                .set("border-radius", "9999px")
                .set("font-size", "0.75rem")
                .set("font-weight", "600")
                .set("background", getDifficultyColor(question.getDifficulty()) + "22")
                .set("color", getDifficultyColor(question.getDifficulty()));

        headerRow.add(numberBadge, name, diffBadge);

        if (question.getMaxPoints() > 0) {
            Span points = new Span(String.format("%.0f pts", question.getMaxPoints()));
            points.getStyle()
                    .set("font-size", "0.8rem")
                    .set("color", "var(--bervan-text-tertiary, #64748b)")
                    .set("margin-left", "8px");
            headerRow.add(points);
        }

        card.add(headerRow);

        // Question details
        if (question.getQuestionDetails() != null && !question.getQuestionDetails().isBlank()) {
            Div questionText = new Div();
            questionText.getElement().setProperty("innerHTML", question.getQuestionDetails());
            questionText.getStyle()
                    .set("margin-top", "8px")
                    .set("padding", "8px 12px")
                    .set("border-left", "3px solid var(--bervan-border-color, #475569)")
                    .set("color", "var(--bervan-text-primary, #e2e8f0)");

            card.add(questionText);
        }

        // Answer (hidden by default, expandable)
        if (question.getAnswerDetails() != null && !question.getAnswerDetails().isBlank()) {
            Div answerContent = new Div();
            answerContent.getElement().setProperty("innerHTML", question.getAnswerDetails());
            answerContent.getStyle()
                    .set("padding", "8px 12px")
                    .set("border-left", "3px solid #10b981")
                    .set("color", "var(--bervan-text-primary, #e2e8f0)");

            Details answerDetails = new Details("Show Answer", answerContent);
            answerDetails.setOpened(false);
            answerDetails.getStyle().set("margin-top", "8px");
            card.add(answerDetails);
        }

        return card;
    }

    private String getDifficultyLabel(int difficulty) {
        return switch (difficulty) {
            case 1 -> "Basic";
            case 2 -> "Junior";
            case 3 -> "Mid";
            case 4 -> "Senior";
            case 5 -> "Expert";
            default -> "Unknown";
        };
    }

    private String getDifficultyColor(int difficulty) {
        return switch (difficulty) {
            case 1 -> "#10b981";
            case 2 -> "#3b82f6";
            case 3 -> "#f59e0b";
            case 4 -> "#f97316";
            case 5 -> "#ef4444";
            default -> "#64748b";
        };
    }

    private Map<Integer, Integer> getAmountOfLvlBasedQuestions(String selectedLvl, int levelNumber) {
        Map<Integer, Integer> result = new HashMap<>();
        List<QuestionConfig> questionConfig = questionConfigService.loadByName(selectedLvl);

        if (!questionConfig.isEmpty()) {
            QuestionConfig config = questionConfig.get(0);
            result.put(1, config.getDifficulty1Amount() != null ? config.getDifficulty1Amount() : 0);
            result.put(2, config.getDifficulty2Amount() != null ? config.getDifficulty2Amount() : 0);
            result.put(3, config.getDifficulty3Amount() != null ? config.getDifficulty3Amount() : 0);
            result.put(4, config.getDifficulty4Amount() != null ? config.getDifficulty4Amount() : 0);
            result.put(5, config.getDifficulty5Amount() != null ? config.getDifficulty5Amount() : 0);
        } else {
            result = getDefaultDistribution(levelNumber);
        }

        return result;
    }

    private Map<Integer, Integer> getDefaultDistribution(int levelNumber) {
        Map<Integer, Integer> result = new HashMap<>();
        // Auto-distribute based on experience level (0-6)
        // Lower experience = more easy questions, higher = more hard
        switch (levelNumber) {
            case 0 -> { result.put(1, 5); result.put(2, 3); result.put(3, 1); result.put(4, 0); result.put(5, 0); }
            case 1 -> { result.put(1, 4); result.put(2, 4); result.put(3, 2); result.put(4, 0); result.put(5, 0); }
            case 2 -> { result.put(1, 2); result.put(2, 4); result.put(3, 3); result.put(4, 1); result.put(5, 0); }
            case 3 -> { result.put(1, 1); result.put(2, 3); result.put(3, 4); result.put(4, 2); result.put(5, 0); }
            case 4 -> { result.put(1, 0); result.put(2, 2); result.put(3, 3); result.put(4, 3); result.put(5, 2); }
            case 5 -> { result.put(1, 0); result.put(2, 1); result.put(3, 2); result.put(4, 4); result.put(5, 3); }
            default -> { result.put(1, 0); result.put(2, 0); result.put(3, 2); result.put(4, 4); result.put(5, 4); }
        }
        return result;
    }

    private Integer getAmountOfSpringSecurityQuestions(String selectedLvl, int levelNumber) {
        List<QuestionConfig> questionConfig = questionConfigService.loadByName(selectedLvl);

        if (!questionConfig.isEmpty()) {
            return questionConfig.get(0).getSpringSecurityAmount();
        }
        // Default: spring security from level 3+
        return levelNumber >= 3 ? 2 : 0;
    }
}
