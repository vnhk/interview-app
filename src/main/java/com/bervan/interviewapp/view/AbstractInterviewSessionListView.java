package com.bervan.interviewapp.view;

import com.bervan.common.view.AbstractPageView;
import com.bervan.interviewapp.session.InterviewSession;
import com.bervan.interviewapp.session.InterviewSessionService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractInterviewSessionListView extends AbstractPageView {
    public static final String ROUTE_NAME = "interview-app/interview-sessions";
    private final InterviewAppPageLayout pageLayout;

    @Autowired
    private InterviewSessionService sessionService;

    public AbstractInterviewSessionListView() {
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

        H3 title = new H3("Interview Sessions");

        List<InterviewSession> sessions = sessionService.loadAllSessions().stream()
                .sorted(Comparator.comparing(InterviewSession::getModificationDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        Grid<InterviewSession> grid = new Grid<>();
        grid.setItems(sessions);
        grid.setWidthFull();

        grid.addColumn(InterviewSession::getCandidateName)
                .setHeader("Candidate").setSortable(true).setResizable(true);

        grid.addColumn(InterviewSession::getConfigName)
                .setHeader("Config").setSortable(true).setResizable(true);

        grid.addColumn(new ComponentRenderer<>(session -> {
            Span badge = new Span(session.getStatus());
            badge.getStyle()
                    .set("padding", "2px 10px")
                    .set("border-radius", "9999px")
                    .set("font-size", "0.75rem")
                    .set("font-weight", "600");
            if ("IN_PROGRESS".equals(session.getStatus())) {
                badge.getStyle().set("background", "rgba(59,130,246, 0.2)").set("color", "#3b82f6");
            } else {
                badge.getStyle().set("background", "rgba(16,185,129, 0.2)").set("color", "#10b981");
            }
            return badge;
        })).setHeader("Status").setSortable(true);

        grid.addColumn(InterviewSession::getTotalQuestions)
                .setHeader("Questions").setSortable(true);

        grid.addColumn(InterviewSession::getMainTags)
                .setHeader("Main Tags").setResizable(true);

        grid.addColumn(s -> {
            if (s.getModificationDate() != null) {
                return s.getModificationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            return "—";
        }).setHeader("Date").setSortable(true);

        grid.addItemClickListener(event -> {
            InterviewSession s = event.getItem();
            getUI().ifPresent(ui -> ui.navigate(
                    AbstractInterviewSessionView.ROUTE_NAME + "/" + s.getId().toString()));
        });

        grid.getStyle().set("cursor", "pointer");

        add(title, grid);
    }
}
