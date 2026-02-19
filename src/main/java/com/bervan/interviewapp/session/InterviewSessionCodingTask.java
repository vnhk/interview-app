package com.bervan.interviewapp.session;

import com.bervan.common.model.BervanOwnedBaseEntity;
import com.bervan.common.model.PersistableTableOwnedData;
import com.bervan.interviewapp.codingtask.CodingTask;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class InterviewSessionCodingTask extends BervanOwnedBaseEntity<UUID> implements PersistableTableOwnedData<UUID> {
    @Id
    private UUID id;
    private Integer taskNumber;
    @Lob
    private String notes;
    private Double score;
    private LocalDateTime modificationDate;
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private InterviewSession session;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coding_task_id")
    private CodingTask codingTask;

    public InterviewSessionCodingTask() {
    }

    @Override
    public String getTableFilterableColumnValue() {
        return codingTask != null ? codingTask.getName() : "";
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

    public Integer getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(Integer taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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

    public CodingTask getCodingTask() {
        return codingTask;
    }

    public void setCodingTask(CodingTask codingTask) {
        this.codingTask = codingTask;
    }
}
