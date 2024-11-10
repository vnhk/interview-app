package com.bervan.interviewapp.pocketitem;

import com.bervan.common.user.User;
import com.bervan.history.model.AbstractBaseHistoryEntity;
import com.bervan.history.model.HistoryField;
import com.bervan.history.model.HistoryOwnerEntity;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.common.model.PersistableTableData;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@HistorySupported

public class HistoryCodingTask implements AbstractBaseHistoryEntity<UUID>, PersistableTableData<UUID>, ExcelIEEntity<UUID> {
    @HistoryField
    private String name;
    @HistoryField
    @Lob
    private String initialCode;
    @HistoryField
    @Lob
    private String exampleCode;
    @HistoryField
    @Lob
    private String exampleCodeDetails;
    @HistoryField
    @Lob
    private String questions;
    private LocalDateTime updateDate;
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User owner;

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public void setOwner(User user) {
        this.owner = user;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @HistoryOwnerEntity
    private CodingTask codingTask;

    public HistoryCodingTask(String name, String initialCode, String exampleCode, String exampleCodeDetails, String questions, LocalDateTime modificationDate) {
        this.name = name;
        this.initialCode = initialCode;
        this.exampleCode = exampleCode;
        this.exampleCodeDetails = exampleCodeDetails;
        this.questions = questions;
        this.updateDate = modificationDate;
    }

    public HistoryCodingTask() {

    }

    public String getTableFilterableColumnValue() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }

    public String getExampleCode() {
        return exampleCode;
    }

    public void setExampleCode(String exampleCode) {
        this.exampleCode = exampleCode;
    }

    public String getExampleCodeDetails() {
        return exampleCodeDetails;
    }

    public void setExampleCodeDetails(String exampleCodeDetails) {
        this.exampleCodeDetails = exampleCodeDetails;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public CodingTask getCodingTask() {
        return codingTask;
    }

    public void setCodingTask(CodingTask codingTask) {
        this.codingTask = codingTask;
    }
}