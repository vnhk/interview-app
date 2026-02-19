package com.bervan.interviewapp.view;

import com.bervan.common.model.BaseOneValue;
import com.bervan.common.onevalue.OneValueService;
import com.bervan.common.view.AbstractPageView;
import com.bervan.interviewapp.codingtask.CodingTask;
import com.bervan.interviewapp.codingtask.CodingTaskService;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.interviewapp.questionconfig.QuestionConfig;
import com.bervan.interviewapp.questionconfig.QuestionConfigService;
import com.bervan.interviewapp.session.InterviewSession;
import com.bervan.interviewapp.session.InterviewSessionCodingTask;
import com.bervan.interviewapp.session.InterviewSessionQuestion;
import com.bervan.interviewapp.session.InterviewSessionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class AbstractStartInterviewView extends AbstractPageView {
    public static final String ROUTE_NAME = "interview-app/interview-process";
    private final InterviewAppPageLayout pageLayout;

    @Autowired
    private QuestionConfigService questionConfigService;
    @Autowired
    private InterviewQuestionService questionService;
    @Autowired
    private InterviewSessionService sessionService;
    @Autowired
    private CodingTaskService codingTaskService;
    @Autowired
    private OneValueService oneValueService;

    private final List<Question> selectedQuestions = new ArrayList<>();
    private final List<CodingTask> selectedCodingTasks = new ArrayList<>();
    private VerticalLayout reviewListLayout;
    private Div warningsDiv;
    private Button startSessionButton;

    public AbstractStartInterviewView() {
        pageLayout = new InterviewAppPageLayout(ROUTE_NAME);
        add(pageLayout);
    }

    @Override
    protected void onAttach(com.vaadin.flow.component.AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        buildContent();
    }

    private void buildContent() {
        getChildren().filter(c -> c != pageLayout).toList().forEach(this::remove);

        // --- Controls ---
        ComboBox<QuestionConfig> configCombo = new ComboBox<>("Interview Config");
        List<QuestionConfig> configs = questionConfigService.load(PageRequest.of(0, 10000)).stream()
                .map(e -> (QuestionConfig) e)
                .collect(Collectors.toList());
        configCombo.setItems(configs);
        configCombo.setItemLabelGenerator(QuestionConfig::getName);
        configCombo.setWidth("250px");

        TextField candidateNameField = new TextField("Candidate Name");
        candidateNameField.setWidth("250px");

        IntegerField totalQuestionsField = new IntegerField("Total Questions");
        totalQuestionsField.setMin(1);
        totalQuestionsField.setMax(200);
        totalQuestionsField.setValue(20);
        totalQuestionsField.setWidth("150px");

        Set<String> allTags = questionService.getAllDistinctTags();

        MultiSelectComboBox<String> mainTagsCombo = new MultiSelectComboBox<>("Main Tags (1-3)");
        mainTagsCombo.setItems(allTags);
        mainTagsCombo.setWidth("300px");

        MultiSelectComboBox<String> secondaryTagsCombo = new MultiSelectComboBox<>("Secondary Tags (1-5)");
        secondaryTagsCombo.setItems(allTags);
        secondaryTagsCombo.setWidth("300px");

        Button generateButton = new Button("Generate Questions", new Icon(VaadinIcon.MAGIC));
        generateButton.addClassName("option-button");

        HorizontalLayout row1 = new HorizontalLayout(configCombo, candidateNameField, totalQuestionsField);
        row1.setAlignItems(FlexComponent.Alignment.END);

        HorizontalLayout row2 = new HorizontalLayout(mainTagsCombo, secondaryTagsCombo, generateButton);
        row2.setAlignItems(FlexComponent.Alignment.END);

        warningsDiv = new Div();
        warningsDiv.setWidthFull();
        warningsDiv.setVisible(false);

        reviewListLayout = new VerticalLayout();
        reviewListLayout.setWidthFull();
        reviewListLayout.setPadding(false);
        reviewListLayout.setVisible(false);

        startSessionButton = new Button("Start Interview Session", new Icon(VaadinIcon.PLAY));
        startSessionButton.addClassName("option-button");
        startSessionButton.setVisible(false);

        generateButton.addClickListener(event -> {
            QuestionConfig config = configCombo.getValue();
            if (config == null) {
                showWarningNotification("Please select an interview config.");
                return;
            }
            Set<String> mainTags = mainTagsCombo.getValue();
            Set<String> secondaryTags = secondaryTagsCombo.getValue();

            if (mainTags.isEmpty()) {
                showWarningNotification("Please select at least 1 main tag.");
                return;
            }
            if (mainTags.size() > 3) {
                showWarningNotification("Select at most 3 main tags.");
                return;
            }
            if (secondaryTags.size() > 5) {
                showWarningNotification("Select at most 5 secondary tags.");
                return;
            }

            int totalQuestions = totalQuestionsField.getValue() != null ? totalQuestionsField.getValue() : 20;

            generateQuestionList(config, new ArrayList<>(mainTags), new ArrayList<>(secondaryTags), totalQuestions);
            generateCodingTaskList(config);
            renderReviewList();
        });

        startSessionButton.addClickListener(event -> {
            if (selectedQuestions.isEmpty()) {
                showWarningNotification("No questions selected.");
                return;
            }

            String candidateName = candidateNameField.getValue();
            if (candidateName == null || candidateName.isBlank()) {
                candidateName = "Unknown";
            }

            QuestionConfig config = configCombo.getValue();
            Set<String> mainTags = mainTagsCombo.getValue();
            Set<String> secondaryTags = secondaryTagsCombo.getValue();

            // Load plan template
            String planTemplate = "";
            List<? extends BaseOneValue> planValues = oneValueService.loadByKey("interview-plan");
            if (!planValues.isEmpty() && planValues.get(0).getContent() != null) {
                planTemplate = planValues.get(0).getContent();
            }

            InterviewSession session = new InterviewSession();
            session.setId(UUID.randomUUID());
            session.setCandidateName(candidateName);
            session.setConfigName(config != null ? config.getName() : "");
            session.setTotalQuestions(selectedQuestions.size());
            session.setMainTags(String.join(", ", mainTags));
            session.setSecondaryTags(String.join(", ", secondaryTags));
            session.setStatus("IN_PROGRESS");
            session.setNotes("");
            session.setPlanTemplate(planTemplate);

            List<InterviewSessionQuestion> sessionQuestions = new ArrayList<>();
            for (int i = 0; i < selectedQuestions.size(); i++) {
                InterviewSessionQuestion sq = new InterviewSessionQuestion();
                sq.setId(UUID.randomUUID());
                sq.setQuestionNumber(i + 1);
                sq.setQuestion(selectedQuestions.get(i));
                sq.setAnswerStatus("NOT_ASKED");
                sq.setSession(session);
                sessionQuestions.add(sq);
            }
            session.setSessionQuestions(sessionQuestions);

            List<InterviewSessionCodingTask> sessionCodingTasks = new ArrayList<>();
            for (int i = 0; i < selectedCodingTasks.size(); i++) {
                InterviewSessionCodingTask sct = new InterviewSessionCodingTask();
                sct.setId(UUID.randomUUID());
                sct.setTaskNumber(i + 1);
                sct.setCodingTask(selectedCodingTasks.get(i));
                sct.setSession(session);
                sessionCodingTasks.add(sct);
            }
            session.setSessionCodingTasks(sessionCodingTasks);

            sessionService.save(session);

            getUI().ifPresent(ui -> ui.navigate(
                    AbstractInterviewSessionView.ROUTE_NAME + "/" + session.getId().toString()));
        });

        add(row1, row2, warningsDiv, reviewListLayout, startSessionButton);
    }

    private void generateCodingTaskList(QuestionConfig config) {
        selectedCodingTasks.clear();
        int amount = config.getCodingTasksAmount() != null ? config.getCodingTasksAmount() : 0;
        if (amount <= 0) return;

        List<CodingTask> allTasks = codingTaskService.load(PageRequest.of(0, 10000)).stream()
                .map(e -> (CodingTask) e)
                .collect(Collectors.toList());

        selectedCodingTasks.addAll(pickRandom(allTasks, amount, new Random()));
    }

    private void generateQuestionList(QuestionConfig config, List<String> mainTags, List<String> secondaryTags, int totalQuestions) {
        selectedQuestions.clear();
        List<String> warnings = new ArrayList<>();

        int mainCount = (int) Math.ceil(totalQuestions * 0.6);
        int secondaryCount = totalQuestions - mainCount;

        if (secondaryTags.isEmpty()) {
            mainCount = totalQuestions;
            secondaryCount = 0;
        }

        Map<Integer, Integer> diffPercents = new LinkedHashMap<>();
        diffPercents.put(1, config.getDifficulty1Percent() != null ? config.getDifficulty1Percent() : 0);
        diffPercents.put(2, config.getDifficulty2Percent() != null ? config.getDifficulty2Percent() : 0);
        diffPercents.put(3, config.getDifficulty3Percent() != null ? config.getDifficulty3Percent() : 0);
        diffPercents.put(4, config.getDifficulty4Percent() != null ? config.getDifficulty4Percent() : 0);
        diffPercents.put(5, config.getDifficulty5Percent() != null ? config.getDifficulty5Percent() : 0);

        Random random = new Random();
        Set<UUID> usedIds = new HashSet<>();

        for (Map.Entry<Integer, Integer> entry : diffPercents.entrySet()) {
            int difficulty = entry.getKey();
            int percent = entry.getValue();
            if (percent <= 0) continue;

            int needed = (int) Math.ceil(mainCount * percent / 100.0);
            List<Question> pool = questionService.findByTagsAndDifficulty(mainTags, difficulty)
                    .stream().filter(q -> !usedIds.contains(q.getId())).collect(Collectors.toList());

            List<Question> picked = pickRandom(pool, needed, random);
            picked.forEach(q -> usedIds.add(q.getId()));
            selectedQuestions.addAll(picked);

            if (picked.size() < needed) {
                warnings.add("Missing " + (needed - picked.size()) + " main-tag questions at level " + difficulty
                        + " for tags: " + String.join(", ", mainTags));
            }
        }

        if (secondaryCount > 0 && !secondaryTags.isEmpty()) {
            for (Map.Entry<Integer, Integer> entry : diffPercents.entrySet()) {
                int difficulty = entry.getKey();
                int percent = entry.getValue();
                if (percent <= 0) continue;

                int needed = (int) Math.ceil(secondaryCount * percent / 100.0);
                List<Question> pool = questionService.findByTagsAndDifficulty(secondaryTags, difficulty)
                        .stream().filter(q -> !usedIds.contains(q.getId())).collect(Collectors.toList());

                List<Question> picked = pickRandom(pool, needed, random);
                picked.forEach(q -> usedIds.add(q.getId()));
                selectedQuestions.addAll(picked);

                if (picked.size() < needed) {
                    warnings.add("Missing " + (needed - picked.size()) + " secondary-tag questions at level " + difficulty
                            + " for tags: " + String.join(", ", secondaryTags));
                }
            }
        }

        warningsDiv.removeAll();
        if (!warnings.isEmpty()) {
            warningsDiv.setVisible(true);
            warningsDiv.getStyle()
                    .set("padding", "12px 16px")
                    .set("margin-top", "8px")
                    .set("border-radius", "8px")
                    .set("background", "rgba(var(--bervan-warning-rgb, 245,158,11), 0.15)")
                    .set("border", "1px solid rgba(var(--bervan-warning-rgb, 245,158,11), 0.3)");
            H4 warnTitle = new H4("Warnings");
            warnTitle.getStyle().set("margin", "0 0 8px 0").set("color", "#f59e0b");
            warningsDiv.add(warnTitle);
            for (String w : warnings) {
                Div line = new Div();
                line.setText(w);
                line.getStyle().set("color", "var(--bervan-text-secondary, #94a3b8)").set("margin-bottom", "4px");
                warningsDiv.add(line);
            }
        } else {
            warningsDiv.setVisible(false);
        }
    }

    private void renderReviewList() {
        reviewListLayout.removeAll();
        reviewListLayout.setVisible(true);
        startSessionButton.setVisible(true);

        // --- Questions section ---
        H3 questionsHeader = new H3("Review Questions (" + selectedQuestions.size() + ")");
        reviewListLayout.add(questionsHeader);

        for (int i = 0; i < selectedQuestions.size(); i++) {
            Question q = selectedQuestions.get(i);
            int idx = i;
            reviewListLayout.add(buildReviewCard(idx, selectedQuestions, q, true));
        }

        Button addQuestionButton = new Button("Add Question", new Icon(VaadinIcon.PLUS));
        addQuestionButton.addClassName("option-button");
        addQuestionButton.addClickListener(e -> openAddQuestionDialog());
        reviewListLayout.add(addQuestionButton);

        // --- Coding Tasks section ---
        if (!selectedCodingTasks.isEmpty()) {
            H3 codingHeader = new H3("Coding Tasks (" + selectedCodingTasks.size() + ")");
            codingHeader.getStyle().set("margin-top", "20px");
            reviewListLayout.add(codingHeader);

            for (int i = 0; i < selectedCodingTasks.size(); i++) {
                CodingTask ct = selectedCodingTasks.get(i);
                int idx = i;
                reviewListLayout.add(buildCodingTaskReviewCard(idx, ct));
            }
        }

        // --- Summary ---
        Div summary = new Div();
        summary.getStyle()
                .set("margin-top", "16px")
                .set("padding", "12px 16px")
                .set("border-radius", "8px")
                .set("background", "rgba(99, 102, 241, 0.1)");

        Map<Integer, Long> byDifficulty = selectedQuestions.stream()
                .collect(Collectors.groupingBy(Question::getDifficulty, Collectors.counting()));
        StringBuilder sb = new StringBuilder("Total: " + selectedQuestions.size() + " questions");
        if (!selectedCodingTasks.isEmpty()) {
            sb.append(" + ").append(selectedCodingTasks.size()).append(" coding tasks");
        }
        sb.append(" — ");
        byDifficulty.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(e -> sb.append("L").append(e.getKey()).append(": ").append(e.getValue()).append("  "));
        summary.add(new Span(sb.toString()));
        reviewListLayout.add(summary);
    }

    private Div buildReviewCard(int idx, List<?> list, Question question, boolean isQuestion) {
        Div card = new Div();
        card.getStyle()
                .set("width", "100%")
                .set("padding", "10px 16px")
                .set("margin-bottom", "6px")
                .set("border-radius", "8px")
                .set("border", "1px solid var(--bervan-border-color, #334155)")
                .set("background", "var(--bervan-surface-hover, rgba(255,255,255,0.03))");

        HorizontalLayout headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(FlexComponent.Alignment.CENTER);

        // Reorder buttons
        HorizontalLayout reorderButtons = new HorizontalLayout();
        reorderButtons.setSpacing(false);
        reorderButtons.getStyle().set("gap", "2px");

        Button upBtn = new Button(new Icon(VaadinIcon.ARROW_UP));
        upBtn.getStyle().set("min-width", "28px").set("padding", "2px").set("cursor", "pointer");
        upBtn.setVisible(idx > 0);
        upBtn.addClickListener(e -> {
            Collections.swap(selectedQuestions, idx, idx - 1);
            renderReviewList();
        });

        Button downBtn = new Button(new Icon(VaadinIcon.ARROW_DOWN));
        downBtn.getStyle().set("min-width", "28px").set("padding", "2px").set("cursor", "pointer");
        downBtn.setVisible(idx < selectedQuestions.size() - 1);
        downBtn.addClickListener(e -> {
            Collections.swap(selectedQuestions, idx, idx + 1);
            renderReviewList();
        });

        reorderButtons.add(upBtn, downBtn);

        Span numberBadge = new Span("#" + (idx + 1));
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

        Span tags = new Span(question.getTags() != null ? question.getTags() : "");
        tags.getStyle()
                .set("font-size", "0.8rem")
                .set("color", "var(--bervan-text-tertiary, #64748b)")
                .set("margin-left", "8px");

        Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        removeBtn.getStyle().set("color", "#ef4444").set("cursor", "pointer");
        removeBtn.addClickListener(e -> {
            selectedQuestions.remove(idx);
            renderReviewList();
        });

        headerRow.add(reorderButtons, numberBadge, name, diffBadge, tags);

        if (question.getMaxPoints() > 0) {
            Span points = new Span(String.format("%.0f pts", question.getMaxPoints()));
            points.getStyle()
                    .set("font-size", "0.8rem")
                    .set("color", "var(--bervan-text-tertiary, #64748b)")
                    .set("margin-left", "8px");
            headerRow.add(points);
        }

        headerRow.add(removeBtn);
        card.add(headerRow);

        if (question.getAnswerDetails() != null && !question.getAnswerDetails().isBlank()) {
            Div answerContent = new Div();
            answerContent.getElement().setProperty("innerHTML", question.getAnswerDetails());
            answerContent.getStyle()
                    .set("padding", "8px 12px")
                    .set("border-left", "3px solid #10b981")
                    .set("color", "var(--bervan-text-primary, #e2e8f0)");

            Details answerDetails = new Details("Show Answer", answerContent);
            answerDetails.setOpened(false);
            answerDetails.getStyle().set("margin-top", "6px");
            card.add(answerDetails);
        }

        return card;
    }

    private Div buildCodingTaskReviewCard(int idx, CodingTask task) {
        Div card = new Div();
        card.getStyle()
                .set("width", "100%")
                .set("padding", "10px 16px")
                .set("margin-bottom", "6px")
                .set("border-radius", "8px")
                .set("border", "1px solid var(--bervan-border-color, #334155)")
                .set("background", "rgba(168, 85, 247, 0.05)");

        HorizontalLayout headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout reorderButtons = new HorizontalLayout();
        reorderButtons.setSpacing(false);
        reorderButtons.getStyle().set("gap", "2px");

        Button upBtn = new Button(new Icon(VaadinIcon.ARROW_UP));
        upBtn.getStyle().set("min-width", "28px").set("padding", "2px").set("cursor", "pointer");
        upBtn.setVisible(idx > 0);
        upBtn.addClickListener(e -> {
            Collections.swap(selectedCodingTasks, idx, idx - 1);
            renderReviewList();
        });

        Button downBtn = new Button(new Icon(VaadinIcon.ARROW_DOWN));
        downBtn.getStyle().set("min-width", "28px").set("padding", "2px").set("cursor", "pointer");
        downBtn.setVisible(idx < selectedCodingTasks.size() - 1);
        downBtn.addClickListener(e -> {
            Collections.swap(selectedCodingTasks, idx, idx + 1);
            renderReviewList();
        });

        reorderButtons.add(upBtn, downBtn);

        Span numberBadge = new Span("#" + (idx + 1));
        numberBadge.getStyle()
                .set("font-weight", "700")
                .set("min-width", "36px")
                .set("color", "#a855f7");

        Span name = new Span(task.getName() != null ? task.getName() : "—");
        name.getStyle()
                .set("font-weight", "600")
                .set("font-size", "1.05rem")
                .set("flex-grow", "1");

        Span badge = new Span("Coding Task");
        badge.getStyle()
                .set("padding", "2px 10px")
                .set("border-radius", "9999px")
                .set("font-size", "0.75rem")
                .set("font-weight", "600")
                .set("background", "#a855f722")
                .set("color", "#a855f7");

        Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        removeBtn.getStyle().set("color", "#ef4444").set("cursor", "pointer");
        removeBtn.addClickListener(e -> {
            selectedCodingTasks.remove(idx);
            renderReviewList();
        });

        headerRow.add(reorderButtons, numberBadge, name, badge, removeBtn);
        card.add(headerRow);

        return card;
    }

    private void openAddQuestionDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setHeaderTitle("Add Question");

        TextField searchField = new TextField("Search by name");
        searchField.setWidthFull();

        VerticalLayout resultsList = new VerticalLayout();
        resultsList.setPadding(false);
        resultsList.setWidthFull();

        Set<UUID> usedIds = selectedQuestions.stream().map(Question::getId).collect(Collectors.toSet());

        searchField.addValueChangeListener(e -> {
            resultsList.removeAll();
            String query = e.getValue();
            if (query == null || query.length() < 2) return;

            List<Question> allQuestions = questionService.load(PageRequest.of(0, 10000)).stream()
                    .map(q -> (Question) q)
                    .filter(q -> !usedIds.contains(q.getId()))
                    .filter(q -> q.getName() != null && q.getName().toLowerCase().contains(query.toLowerCase()))
                    .limit(20)
                    .collect(Collectors.toList());

            for (Question q : allQuestions) {
                HorizontalLayout row = new HorizontalLayout();
                row.setWidthFull();
                row.setAlignItems(FlexComponent.Alignment.CENTER);

                Span qName = new Span(q.getName() + " (L" + q.getDifficulty() + ")");
                qName.getStyle().set("flex-grow", "1");

                Span qTags = new Span(q.getTags() != null ? q.getTags() : "");
                qTags.getStyle().set("color", "var(--bervan-text-tertiary, #64748b)").set("font-size", "0.85rem");

                Button addBtn = new Button("Add", new Icon(VaadinIcon.PLUS));
                addBtn.addClickListener(ev -> {
                    selectedQuestions.add(q);
                    usedIds.add(q.getId());
                    dialog.close();
                    renderReviewList();
                });

                row.add(qName, qTags, addBtn);
                resultsList.add(row);
            }

            if (allQuestions.isEmpty()) {
                resultsList.add(new Span("No matching questions found."));
            }
        });

        VerticalLayout content = new VerticalLayout(searchField, resultsList);
        content.setPadding(false);
        dialog.add(content);

        Button closeBtn = new Button("Close", e -> dialog.close());
        dialog.getFooter().add(closeBtn);

        dialog.open();
    }

    private <T> List<T> pickRandom(List<T> pool, int amount, Random random) {
        if (amount >= pool.size()) {
            return new ArrayList<>(pool);
        }
        List<T> shuffled = new ArrayList<>(pool);
        Collections.shuffle(shuffled, random);
        return shuffled.subList(0, amount);
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
}
