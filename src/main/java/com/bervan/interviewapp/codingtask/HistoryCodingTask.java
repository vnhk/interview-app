package com.bervan.interviewapp.codingtask;

import com.bervan.common.model.BervanHistoryEntity;
import com.bervan.common.model.PersistableTableData;
import com.bervan.history.model.HistoryField;
import com.bervan.history.model.HistoryOwnerEntity;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@HistorySupported
public class HistoryCodingTask extends BervanHistoryEntity<UUID> implements  PersistableTableData<UUID>, ExcelIEEntity<UUID> {
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
    private UUID id;

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