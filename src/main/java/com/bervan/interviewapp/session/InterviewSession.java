package com.bervan.interviewapp.session;

import com.bervan.common.model.BervanOwnedBaseEntity;
import com.bervan.common.model.PersistableTableOwnedData;
import com.bervan.history.model.HistoryCollection;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@HistorySupported
public class InterviewSession extends BervanOwnedBaseEntity<UUID> implements PersistableTableOwnedData<UUID>, ExcelIEEntity<UUID> {
    @Id
    private UUID id;
    private String candidateName;
    private String configName;
    private Integer totalQuestions;
    private String mainTags;
    private String secondaryTags;
    @Lob
    private String notes;
    @Lob
    private String planTemplate;
    private String status;
    private LocalDateTime modificationDate;
    private boolean deleted;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "session", fetch = FetchType.EAGER)
    private List<InterviewSessionQuestion> sessionQuestions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "session", fetch = FetchType.EAGER)
    private List<InterviewSessionCodingTask> sessionCodingTasks = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @HistoryCollection(historyClass = HistoryInterviewSession.class)
    private Set<HistoryInterviewSession> history = new HashSet<>();

    public InterviewSession() {
    }

    @Override
    public String getTableFilterableColumnValue() {
        return candidateName;
    }

    public String getName() {
        return candidateName;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getMainTags() {
        return mainTags;
    }

    public void setMainTags(String mainTags) {
        this.mainTags = mainTags;
    }

    public String getSecondaryTags() {
        return secondaryTags;
    }

    public void setSecondaryTags(String secondaryTags) {
        this.secondaryTags = secondaryTags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<InterviewSessionQuestion> getSessionQuestions() {
        return sessionQuestions;
    }

    public void setSessionQuestions(List<InterviewSessionQuestion> sessionQuestions) {
        this.sessionQuestions = sessionQuestions;
    }

    public Set<HistoryInterviewSession> getHistory() {
        return history;
    }

    public void setHistory(Set<HistoryInterviewSession> history) {
        this.history = history;
    }

    public String getPlanTemplate() {
        return planTemplate;
    }

    public void setPlanTemplate(String planTemplate) {
        this.planTemplate = planTemplate;
    }

    public List<InterviewSessionCodingTask> getSessionCodingTasks() {
        return sessionCodingTasks;
    }

    public void setSessionCodingTasks(List<InterviewSessionCodingTask> sessionCodingTasks) {
        this.sessionCodingTasks = sessionCodingTasks;
    }
}
