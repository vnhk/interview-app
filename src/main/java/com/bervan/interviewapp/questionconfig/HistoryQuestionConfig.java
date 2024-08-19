package com.bervan.interviewapp.questionconfig;

import com.bervan.history.model.AbstractBaseHistoryEntity;
import com.bervan.history.model.HistoryField;
import com.bervan.history.model.HistoryOwnerEntity;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.common.model.PersistableTableData;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@HistorySupported
public class HistoryQuestionConfig implements AbstractBaseHistoryEntity<UUID>, PersistableTableData, ExcelIEEntity<UUID> {
    @HistoryField
    private String name;
    @HistoryField
    private Integer difficulty1Amount;
    @HistoryField
    private Integer difficulty2Amount;
    @HistoryField
    private Integer difficulty3Amount;
    @HistoryField
    private Integer difficulty4Amount;
    @HistoryField
    private Integer difficulty5Amount;
    @HistoryField
    private Integer springSecurityAmount;
    private LocalDateTime updateDate;
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @HistoryOwnerEntity
    private QuestionConfig questionConfig;

    public HistoryQuestionConfig() {

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

    public Integer getDifficulty5Amount() {
        return difficulty5Amount;
    }

    public void setDifficulty5Amount(Integer difficulty5Amount) {
        this.difficulty5Amount = difficulty5Amount;
    }

    public Integer getDifficulty4Amount() {
        return difficulty4Amount;
    }

    public void setDifficulty4Amount(Integer difficulty4Amount) {
        this.difficulty4Amount = difficulty4Amount;
    }

    public Integer getDifficulty3Amount() {
        return difficulty3Amount;
    }

    public void setDifficulty3Amount(Integer difficulty3Amount) {
        this.difficulty3Amount = difficulty3Amount;
    }

    public Integer getDifficulty2Amount() {
        return difficulty2Amount;
    }

    public void setDifficulty2Amount(Integer difficulty2Amount) {
        this.difficulty2Amount = difficulty2Amount;
    }

    public Integer getDifficulty1Amount() {
        return difficulty1Amount;
    }

    public void setDifficulty1Amount(Integer difficulty1Amount) {
        this.difficulty1Amount = difficulty1Amount;
    }

    public Integer getSpringSecurityAmount() {
        return springSecurityAmount;
    }

    public void setSpringSecurityAmount(Integer springSecurityAmount) {
        this.springSecurityAmount = springSecurityAmount;
    }
}