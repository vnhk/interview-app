package com.bervan.interviewapp.session;

import com.bervan.common.model.BervanOwnedBaseEntity;
import com.bervan.common.model.PersistableTableOwnedData;
import com.bervan.history.model.HistoryCollection;
import com.bervan.history.model.HistorySupported;
import com.bervan.interviewapp.interviewquestions.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@HistorySupported
public class InterviewSessionQuestion extends BervanOwnedBaseEntity<UUID> implements PersistableTableOwnedData<UUID> {
    @Id
    private UUID id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "interviewSessionQuestion")
    @HistoryCollection(historyClass = HistoryInterviewSessionQuestion.class)
    private Set<HistoryInterviewSessionQuestion> history = new HashSet<>();
    private Integer questionNumber;
    private Double score;
    @Lob
    @Size(max = 5000)
    @Column(columnDefinition = "MEDIUMTEXT")
    private String notes;
    private String answerStatus;
    private LocalDateTime modificationDate;
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private InterviewSession session;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Question question;

    public InterviewSessionQuestion() {
    }

    @Override
    public String getTableFilterableColumnValue() {
        return question != null ? question.getName() : "";
    }

    public String getName() {
        return getTableFilterableColumnValue();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(String answerStatus) {
        this.answerStatus = answerStatus;
    }

    @Override
    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    @Override
    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public InterviewSession getSession() {
        return session;
    }

    public void setSession(InterviewSession session) {
        this.session = session;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<HistoryInterviewSessionQuestion> getHistory() {
        return history;
    }

    public void setHistory(Set<HistoryInterviewSessionQuestion> history) {
        this.history = history;
    }
}
