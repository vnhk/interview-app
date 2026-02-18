package com.bervan.interviewapp.questionconfig;

import com.bervan.common.model.BervanHistoryOwnedEntity;
import com.bervan.common.model.PersistableTableOwnedData;
import com.bervan.history.model.HistoryField;
import com.bervan.history.model.HistoryOwnerEntity;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@HistorySupported
public class HistoryQuestionConfig extends BervanHistoryOwnedEntity<UUID> implements PersistableTableOwnedData<UUID>, ExcelIEEntity<UUID> {
    @HistoryField
    private String name;
    @HistoryField
    private Integer difficulty1Percent;
    @HistoryField
    private Integer difficulty2Percent;
    @HistoryField
    private Integer difficulty3Percent;
    @HistoryField
    private Integer difficulty4Percent;
    @HistoryField
    private Integer difficulty5Percent;
    private LocalDateTime updateDate;
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @HistoryOwnerEntity
    private QuestionConfig questionConfig;

    public HistoryQuestionConfig() {

    }

    public String getTableFilterableColumnValue() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public QuestionConfig getCodingTask() {
        return questionConfig;
    }

    public void setCodingTask(QuestionConfig questionConfig) {
        this.questionConfig = questionConfig;
    }

    public Integer getDifficulty1Percent() {
        return difficulty1Percent;
    }

    public void setDifficulty1Percent(Integer difficulty1Percent) {
        this.difficulty1Percent = difficulty1Percent;
    }

    public Integer getDifficulty2Percent() {
        return difficulty2Percent;
    }

    public void setDifficulty2Percent(Integer difficulty2Percent) {
        this.difficulty2Percent = difficulty2Percent;
    }

    public Integer getDifficulty3Percent() {
        return difficulty3Percent;
    }

    public void setDifficulty3Percent(Integer difficulty3Percent) {
        this.difficulty3Percent = difficulty3Percent;
    }

    public Integer getDifficulty4Percent() {
        return difficulty4Percent;
    }

    public void setDifficulty4Percent(Integer difficulty4Percent) {
        this.difficulty4Percent = difficulty4Percent;
    }

    public Integer getDifficulty5Percent() {
        return difficulty5Percent;
    }

    public void setDifficulty5Percent(Integer difficulty5Percent) {
        this.difficulty5Percent = difficulty5Percent;
    }
}
