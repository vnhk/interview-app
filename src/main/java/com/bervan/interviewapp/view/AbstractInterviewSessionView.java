package com.bervan.interviewapp.view;

import com.bervan.common.view.AbstractPageView;
import com.bervan.interviewapp.codingtask.CodingTask;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.interviewapp.session.InterviewSession;
import com.bervan.interviewapp.session.InterviewSessionCodingTask;
import com.bervan.interviewapp.session.InterviewSessionQuestion;
import com.bervan.interviewapp.session.InterviewSessionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractInterviewSessionView extends AbstractPageView implements HasUrlParameter<String> {
    public static final String ROUTE_NAME = "interview-app/interview-session";
    private final InterviewAppPageLayout pageLayout;

    @Autowired
    private InterviewSessionService sessionService;

    private InterviewSession session;

    public AbstractInterviewSessionView() {
        pageLayout = new InterviewAppPageLayout(ROUTE_NAME);
        add(pageLayout);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        getChildren().filter(c -> c != pageLayout).toList().forEach(this::remove);

        if (parameter == null || parameter.isBlank()) {
            add(new H3("Session not found"));
            return;
        }

        try {
            UUID sessionId = UUID.fromString(parameter);
            Optional<InterviewSession> found = sessionService.load(PageRequest.of(0, 10000)).stream()
                    .map(e -> (InterviewSession) e)
                    .filter(s -> s.getId().equals(sessionId))
                    .findFirst();

            if (found.isEmpty()) {
                add(new H3("Session not found"));
                return;
            }

            session = found.get();
            buildSessionContent();
        } catch (IllegalArgumentException e) {
            add(new H3("Invalid session ID"));
        }
    }

    private void buildSessionContent() {
        // --- Header ---
        HorizontalLayout headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(FlexComponent.Alignment.CENTER);

        H2 title = new H2("Interview: " + session.getCandidateName());
        title.getStyle().set("margin", "0").set("flex-grow", "1");

        Span statusBadge = new Span(session.getStatus());
        statusBadge.getStyle()
                .set("padding", "4px 12px")
                .set("border-radius", "9999px")
                .set("font-size", "0.8rem")
                .set("font-weight", "600")
                .set("background", "IN_PROGRESS".equals(session.getStatus())
                        ? "rgba(var(--bervan-info-rgb, 59,130,246), 0.2)" : "rgba(var(--bervan-success-rgb, 16,185,129), 0.2)")
                .set("color", "IN_PROGRESS".equals(session.getStatus()) ? "#3b82f6" : "#10b981");

        headerRow.add(title, statusBadge);

        // Info row
        Div infoRow = new Div();
        infoRow.getStyle().set("color", "var(--bervan-text-secondary, #94a3b8)").set("margin-bottom", "16px");
        infoRow.setText("Config: " + session.getConfigName()
                + " | Main tags: " + session.getMainTags()
                + " | Secondary tags: " + (session.getSecondaryTags() != null ? session.getSecondaryTags() : "—")
                + " | Questions: " + session.getTotalQuestions());

        // Global Notes
        TextArea globalNotes = new TextArea("Interview Notes");
        globalNotes.setWidthFull();
        globalNotes.setHeight("120px");
        globalNotes.setValue(session.getNotes() != null ? session.getNotes() : "");

        add(headerRow, infoRow, globalNotes);

        // --- Scripted content from plan template ---
        List<InterviewSessionQuestion> sortedQuestions = session.getSessionQuestions().stream()
                .sorted(Comparator.comparing(InterviewSessionQuestion::getQuestionNumber))
                .collect(Collectors.toList());

        List<InterviewSessionCodingTask> sortedCodingTasks = session.getSessionCodingTasks() != null
                ? session.getSessionCodingTasks().stream()
                .sorted(Comparator.comparing(InterviewSessionCodingTask::getTaskNumber))
                .collect(Collectors.toList())
                : List.of();

        String planTemplate = session.getPlanTemplate();

        if (planTemplate != null && !planTemplate.isBlank()) {
            renderFromTemplate(planTemplate, sortedQuestions, sortedCodingTasks);
        } else {
            // Fallback: just render questions and coding tasks directly
            VerticalLayout questionsLayout = new VerticalLayout();
            questionsLayout.setPadding(false);
            questionsLayout.setWidthFull();
            for (InterviewSessionQuestion sq : sortedQuestions) {
                questionsLayout.add(buildSessionQuestionCard(sq));
            }
            add(questionsLayout);

            if (!sortedCodingTasks.isEmpty()) {
                H3 codingHeader = new H3("Coding Tasks");
                add(codingHeader);
                for (InterviewSessionCodingTask sct : sortedCodingTasks) {
                    add(buildSessionCodingTaskCard(sct));
                }
            }
        }

        // --- Action Buttons ---
        HorizontalLayout actions = new HorizontalLayout();
        actions.setAlignItems(FlexComponent.Alignment.CENTER);

        Button saveButton = new Button("Save Progress", new Icon(VaadinIcon.CHECK));
        saveButton.addClassName("option-button");
        saveButton.addClickListener(e -> {
            session.setNotes(globalNotes.getValue());
            sessionService.save(session);
            showSuccessNotification("Session saved.");
        });

        Button completeButton = new Button("Complete Interview", new Icon(VaadinIcon.FLAG_CHECKERED));
        completeButton.addClassName("option-button");
        completeButton.addClickListener(e -> {
            session.setNotes(globalNotes.getValue());
            session.setStatus("COMPLETED");
            sessionService.save(session);
            showSuccessNotification("Interview completed!");
            getChildren().filter(c -> c != pageLayout).toList().forEach(this::remove);
            buildSessionContent();
        });

        actions.add(saveButton, completeButton);
        add(actions);

        if ("COMPLETED".equals(session.getStatus())) {
            add(buildSummary(sortedQuestions, sortedCodingTasks));
        }
    }

    private void renderFromTemplate(String template, List<InterviewSessionQuestion> questions, List<InterviewSessionCodingTask> codingTasks) {
        // Replace simple text variables
        String text = template
                .replace("{candidateName}", session.getCandidateName() != null ? session.getCandidateName() : "")
                .replace("{configName}", session.getConfigName() != null ? session.getConfigName() : "")
                .replace("{mainTags}", session.getMainTags() != null ? session.getMainTags() : "")
                .replace("{secondaryTags}", session.getSecondaryTags() != null ? session.getSecondaryTags() : "")
                .replace("{totalQuestions}", String.valueOf(session.getTotalQuestions() != null ? session.getTotalQuestions() : 0));

        // Split on {questions} and {codingTasks} placeholders
        Pattern pattern = Pattern.compile("(\\{questions}|\\{codingTasks})");
        Matcher matcher = pattern.matcher(text);

        int lastEnd = 0;
        while (matcher.find()) {
            // Text before placeholder
            String before = text.substring(lastEnd, matcher.start());
            if (!before.isEmpty()) {
                add(buildScriptTextBlock(before));
            }

            String placeholder = matcher.group(1);
            if ("{questions}".equals(placeholder)) {
                VerticalLayout questionsLayout = new VerticalLayout();
                questionsLayout.setPadding(false);
                questionsLayout.setWidthFull();
                for (InterviewSessionQuestion sq : questions) {
                    questionsLayout.add(buildSessionQuestionCard(sq));
                }
                add(questionsLayout);
            } else if ("{codingTasks}".equals(placeholder)) {
                VerticalLayout codingLayout = new VerticalLayout();
                codingLayout.setPadding(false);
                codingLayout.setWidthFull();
                for (InterviewSessionCodingTask sct : codingTasks) {
                    codingLayout.add(buildSessionCodingTaskCard(sct));
                }
                add(codingLayout);
            }

            lastEnd = matcher.end();
        }

        // Text after last placeholder
        String remaining = text.substring(lastEnd);
        if (!remaining.isEmpty()) {
            add(buildScriptTextBlock(remaining));
        }
    }

    private Div buildScriptTextBlock(String text) {
        Div block = new Div();
        block.setText(text);
        block.getStyle()
                .set("white-space", "pre-wrap")
                .set("padding", "12px 16px")
                .set("margin", "8px 0")
                .set("border-radius", "8px")
                .set("background", "rgba(99, 102, 241, 0.06)")
                .set("border-left", "3px solid rgba(99, 102, 241, 0.3)")
                .set("color", "var(--bervan-text-primary, #e2e8f0)")
                .set("font-size", "1rem")
                .set("line-height", "1.6");
        return block;
    }

    private com.vaadin.flow.component.Component buildSessionQuestionCard(InterviewSessionQuestion sq) {
        Question question = sq.getQuestion();

        Div card = new Div();
        card.getStyle()
                .set("width", "100%")
                .set("padding", "12px 16px")
                .set("margin-bottom", "8px")
                .set("border-radius", "8px")
                .set("border", "1px solid var(--bervan-border-color, #334155)")
                .set("background", getCardBackground(sq.getAnswerStatus()));

        HorizontalLayout headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(FlexComponent.Alignment.CENTER);

        Span numberBadge = new Span("#" + sq.getQuestionNumber());
        numberBadge.getStyle()
                .set("font-weight", "700")
                .set("min-width", "36px")
                .set("color", "var(--bervan-text-secondary, #94a3b8)");

        Span name = new Span(question != null && question.getName() != null ? question.getName() : "—");
        name.getStyle()
                .set("font-weight", "600")
                .set("font-size", "1.05rem")
                .set("flex-grow", "1");

        Span diffBadge = new Span("Lvl " + (question != null ? question.getDifficulty() : "?"));
        diffBadge.getStyle()
                .set("padding", "2px 10px")
                .set("border-radius", "9999px")
                .set("font-size", "0.75rem")
                .set("font-weight", "600")
                .set("background", getDifficultyColor(question != null ? question.getDifficulty() : 0) + "22")
                .set("color", getDifficultyColor(question != null ? question.getDifficulty() : 0));

        headerRow.add(numberBadge, name, diffBadge);
        card.add(headerRow);

        if (question != null && question.getQuestionDetails() != null && !question.getQuestionDetails().isBlank()) {
            Div questionText = new Div();
            questionText.getElement().setProperty("innerHTML", question.getQuestionDetails());
            questionText.getStyle()
                    .set("margin-top", "8px")
                    .set("padding", "8px 12px")
                    .set("border-left", "3px solid var(--bervan-border-color, #475569)")
                    .set("color", "var(--bervan-text-primary, #e2e8f0)");
            card.add(questionText);
        }

        if (question != null && question.getAnswerDetails() != null && !question.getAnswerDetails().isBlank()) {
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

        // Answer status buttons
        HorizontalLayout statusRow = new HorizontalLayout();
        statusRow.setAlignItems(FlexComponent.Alignment.CENTER);
        statusRow.getStyle().set("margin-top", "8px");

        String[] statuses = {"NOT_ASKED", "CORRECT", "PARTIAL", "INCORRECT"};
        String[] labels = {"Not Asked", "Correct", "Partial", "Incorrect"};
        String[] colors = {"#64748b", "#10b981", "#f59e0b", "#ef4444"};

        for (int i = 0; i < statuses.length; i++) {
            String statusVal = statuses[i];
            Button btn = new Button(labels[i]);
            btn.getStyle()
                    .set("font-size", "0.8rem")
                    .set("padding", "4px 12px")
                    .set("border-radius", "9999px")
                    .set("cursor", "pointer");

            if (statusVal.equals(sq.getAnswerStatus())) {
                btn.getStyle()
                        .set("background", colors[i] + "33")
                        .set("color", colors[i])
                        .set("border", "1px solid " + colors[i]);
            } else {
                btn.getStyle()
                        .set("background", "transparent")
                        .set("color", "var(--bervan-text-tertiary, #64748b)")
                        .set("border", "1px solid var(--bervan-border-color, #334155)");
            }

            btn.addClickListener(e -> {
                sq.setAnswerStatus(statusVal);
                card.getStyle().set("background", getCardBackground(statusVal));
                statusRow.getChildren().forEach(c -> {
                    if (c instanceof Button b) {
                        b.getStyle()
                                .set("background", "transparent")
                                .set("color", "var(--bervan-text-tertiary, #64748b)")
                                .set("border", "1px solid var(--bervan-border-color, #334155)");
                    }
                });
                btn.getStyle()
                        .set("background", getStatusColor(statusVal) + "33")
                        .set("color", getStatusColor(statusVal))
                        .set("border", "1px solid " + getStatusColor(statusVal));
            });

            statusRow.add(btn);
        }

        NumberField scoreField = new NumberField("Score");
        scoreField.setWidth("80px");
        scoreField.setMin(0);
        scoreField.setMax(question != null ? question.getMaxPoints() : 10);
        scoreField.setValue(sq.getScore() != null ? sq.getScore() : 0.0);
        scoreField.addValueChangeListener(e -> sq.setScore(e.getValue()));

        statusRow.add(scoreField);
        card.add(statusRow);

        TextArea notesField = new TextArea("Notes");
        notesField.setWidthFull();
        notesField.setHeight("60px");
        notesField.setValue(sq.getNotes() != null ? sq.getNotes() : "");
        notesField.addValueChangeListener(e -> sq.setNotes(e.getValue()));
        card.add(notesField);

        return card;
    }

    private com.vaadin.flow.component.Component buildSessionCodingTaskCard(InterviewSessionCodingTask sct) {
        CodingTask task = sct.getCodingTask();

        Div card = new Div();
        card.getStyle()
                .set("width", "100%")
                .set("padding", "12px 16px")
                .set("margin-bottom", "8px")
                .set("border-radius", "8px")
                .set("border", "1px solid var(--bervan-border-color, #334155)")
                .set("background", "rgba(168, 85, 247, 0.05)");

        HorizontalLayout headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(FlexComponent.Alignment.CENTER);

        Span numberBadge = new Span("#" + sct.getTaskNumber());
        numberBadge.getStyle()
                .set("font-weight", "700")
                .set("min-width", "36px")
                .set("color", "#a855f7");

        Span name = new Span(task != null && task.getName() != null ? task.getName() : "—");
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

        headerRow.add(numberBadge, name, badge);
        card.add(headerRow);

        if (task != null) {
            // Initial Code
            if (task.getInitialCode() != null && !task.getInitialCode().isBlank()) {
                Div codeContent = new Div();
                codeContent.getElement().setProperty("innerHTML", "<pre style='margin:0;white-space:pre-wrap;'>" + escapeHtml(task.getInitialCode()) + "</pre>");
                codeContent.getStyle()
                        .set("padding", "8px 12px")
                        .set("background", "rgba(0,0,0,0.2)")
                        .set("border-radius", "6px")
                        .set("font-family", "monospace")
                        .set("font-size", "0.85rem");

                Details codeDetails = new Details("Initial Code", codeContent);
                codeDetails.setOpened(false);
                codeDetails.getStyle().set("margin-top", "8px");
                card.add(codeDetails);
            }

            // Example Solution
            if (task.getExampleCode() != null && !task.getExampleCode().isBlank()) {
                Div solutionContent = new Div();
                solutionContent.getElement().setProperty("innerHTML", "<pre style='margin:0;white-space:pre-wrap;'>" + escapeHtml(task.getExampleCode()) + "</pre>");
                solutionContent.getStyle()
                        .set("padding", "8px 12px")
                        .set("background", "rgba(0,0,0,0.2)")
                        .set("border-radius", "6px")
                        .set("font-family", "monospace")
                        .set("font-size", "0.85rem");

                Details solutionDetails = new Details("Example Solution", solutionContent);
                solutionDetails.setOpened(false);
                solutionDetails.getStyle().set("margin-top", "8px");
                card.add(solutionDetails);
            }

            // Questions for the task
            if (task.getQuestions() != null && !task.getQuestions().isBlank()) {
                Div questionsContent = new Div();
                questionsContent.setText(task.getQuestions());
                questionsContent.getStyle()
                        .set("white-space", "pre-wrap")
                        .set("padding", "8px 12px")
                        .set("border-left", "3px solid #a855f7")
                        .set("color", "var(--bervan-text-primary, #e2e8f0)");

                Details questionsDetails = new Details("Task Questions", questionsContent);
                questionsDetails.setOpened(false);
                questionsDetails.getStyle().set("margin-top", "8px");
                card.add(questionsDetails);
            }
        }

        // Score
        NumberField scoreField = new NumberField("Score");
        scoreField.setWidth("80px");
        scoreField.setMin(0);
        scoreField.setValue(sct.getScore() != null ? sct.getScore() : 0.0);
        scoreField.addValueChangeListener(e -> sct.setScore(e.getValue()));
        scoreField.getStyle().set("margin-top", "8px");
        card.add(scoreField);

        // Notes
        TextArea notesField = new TextArea("Notes");
        notesField.setWidthFull();
        notesField.setHeight("60px");
        notesField.setValue(sct.getNotes() != null ? sct.getNotes() : "");
        notesField.addValueChangeListener(e -> sct.setNotes(e.getValue()));
        card.add(notesField);

        return card;
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private com.vaadin.flow.component.Component buildSummary(List<InterviewSessionQuestion> questions, List<InterviewSessionCodingTask> codingTasks) {
        VerticalLayout summaryLayout = new VerticalLayout();
        summaryLayout.getStyle()
                .set("margin-top", "24px")
                .set("padding", "16px 20px")
                .set("border-radius", "12px")
                .set("background", "rgba(99, 102, 241, 0.1)")
                .set("border", "1px solid rgba(99, 102, 241, 0.2)");

        H3 summaryTitle = new H3("Interview Summary");
        summaryTitle.getStyle().set("margin", "0 0 12px 0");
        summaryLayout.add(summaryTitle);

        // Total score (questions)
        double totalScore = questions.stream()
                .filter(q -> q.getScore() != null)
                .mapToDouble(InterviewSessionQuestion::getScore)
                .sum();
        double maxScore = questions.stream()
                .filter(q -> q.getQuestion() != null)
                .mapToDouble(q -> q.getQuestion().getMaxPoints())
                .sum();

        // Coding tasks score
        double codingScore = codingTasks.stream()
                .filter(ct -> ct.getScore() != null)
                .mapToDouble(InterviewSessionCodingTask::getScore)
                .sum();

        summaryLayout.add(new Span(String.format("Questions Score: %.1f / %.1f (%.0f%%)",
                totalScore, maxScore, maxScore > 0 ? (totalScore / maxScore * 100) : 0)));

        if (!codingTasks.isEmpty()) {
            summaryLayout.add(new Span(String.format("Coding Tasks Score: %.1f", codingScore)));
        }

        summaryLayout.add(new Span(String.format("Combined Score: %.1f", totalScore + codingScore)));

        // By status
        Map<String, Long> byStatus = questions.stream()
                .collect(Collectors.groupingBy(
                        q -> q.getAnswerStatus() != null ? q.getAnswerStatus() : "NOT_ASKED",
                        Collectors.counting()));
        StringBuilder statusSb = new StringBuilder("Answers: ");
        byStatus.forEach((status, count) -> statusSb.append(status.replace("_", " ")).append(": ").append(count).append("  "));
        summaryLayout.add(new Span(statusSb.toString()));

        // By difficulty
        Map<Integer, List<InterviewSessionQuestion>> byDifficulty = questions.stream()
                .filter(q -> q.getQuestion() != null)
                .collect(Collectors.groupingBy(q -> q.getQuestion().getDifficulty()));

        for (Map.Entry<Integer, List<InterviewSessionQuestion>> entry : new TreeMap<>(byDifficulty).entrySet()) {
            int diff = entry.getKey();
            List<InterviewSessionQuestion> qs = entry.getValue();
            double diffScore = qs.stream().filter(q -> q.getScore() != null).mapToDouble(InterviewSessionQuestion::getScore).sum();
            double diffMax = qs.stream().mapToDouble(q -> q.getQuestion().getMaxPoints()).sum();
            summaryLayout.add(new Span(String.format("Level %d: %.1f / %.1f (%d questions)",
                    diff, diffScore, diffMax, qs.size())));
        }

        return summaryLayout;
    }

    private String getCardBackground(String answerStatus) {
        if (answerStatus == null) return "var(--bervan-surface-hover, rgba(255,255,255,0.03))";
        return switch (answerStatus) {
            case "CORRECT" -> "rgba(16, 185, 129, 0.05)";
            case "PARTIAL" -> "rgba(245, 158, 11, 0.05)";
            case "INCORRECT" -> "rgba(239, 68, 68, 0.05)";
            default -> "var(--bervan-surface-hover, rgba(255,255,255,0.03))";
        };
    }

    private String getStatusColor(String status) {
        return switch (status) {
            case "CORRECT" -> "#10b981";
            case "PARTIAL" -> "#f59e0b";
            case "INCORRECT" -> "#ef4444";
            default -> "#64748b";
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
}
