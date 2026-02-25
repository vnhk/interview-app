package com.bervan.interviewapp.session;

import com.bervan.common.model.BervanHistoryOwnedEntity;
import com.bervan.common.model.PersistableTableOwnedData;
import com.bervan.history.model.HistoryField;
import com.bervan.history.model.HistoryOwnerEntity;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@HistorySupported
public class HistoryInterviewSessionQuestion extends BervanHistoryOwnedEntity<UUID> implements PersistableTableOwnedData<UUID>, ExcelIEEntity<UUID> {

    @Id
    private UUID id;

    @HistoryField
    private String answerStatus;
    @HistoryField
    private Double score;
    @HistoryField
    @Lob
    @Size(max = 5000)
    @Column(columnDefinition = "MEDIUMTEXT")
    private String notes;

    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @HistoryOwnerEntity
    private InterviewSessionQuestion interviewSessionQuestion;

    public HistoryInterviewSessionQuestion() {
    }

    @Override
    public String getTableFilterableColumnValue() {
        return interviewSessionQuestion != null ? interviewSessionQuestion.getName() : "";
    }

    public String getName() {
        return getTableFilterableColumnValue();
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    @Override
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(String answerStatus) {
        this.answerStatus = answerStatus;
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

    public InterviewSessionQuestion getInterviewSessionQuestion() {
        return interviewSessionQuestion;
    }

    public void setInterviewSessionQuestion(InterviewSessionQuestion interviewSessionQuestion) {
        this.interviewSessionQuestion = interviewSessionQuestion;
    }
}
