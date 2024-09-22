package com.bervan.interviewapp.questionconfig;

import com.bervan.history.model.AbstractBaseEntity;
import com.bervan.history.model.HistoryCollection;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.common.model.PersistableTableData;
import jakarta.persistence.*;
import org.checkerframework.common.aliasing.qual.Unique;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@HistorySupported
public class QuestionConfig implements AbstractBaseEntity<UUID>, PersistableTableData, ExcelIEEntity<UUID> {
    @Unique
    private String name;
    private Integer difficulty1Amount;
    private Integer difficulty2Amount;
    private Integer difficulty3Amount;
    private Integer difficulty4Amount;
    private Integer difficulty5Amount;
    private Integer springSecurityAmount;
    private LocalDateTime modificationDate;
    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(fetch = FetchType.EAGER)
    @HistoryCollection(historyClass = HistoryQuestionConfig.class)
    private Set<HistoryQuestionConfig> history = new HashSet<>();


    public QuestionConfig() {

    }

    public String getTableFilterableColumnValue() {
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
    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    @Override
    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Set<HistoryQuestionConfig> getHistory() {
        return history;
    }

    public void setHistory(Set<HistoryQuestionConfig> history) {
        this.history = history;
    }


    public Integer getDifficulty1Amount() {
        return difficulty1Amount;
    }

    public void setDifficulty1Amount(Integer difficulty1Amount) {
        this.difficulty1Amount = difficulty1Amount;
    }

    public Integer getDifficulty2Amount() {
        return difficulty2Amount;
    }

    public void setDifficulty2Amount(Integer difficulty2Amount) {
        this.difficulty2Amount = difficulty2Amount;
    }

    public Integer getDifficulty3Amount() {
        return difficulty3Amount;
    }

    public void setDifficulty3Amount(Integer difficulty3Amount) {
        this.difficulty3Amount = difficulty3Amount;
    }

    public Integer getDifficulty4Amount() {
        return difficulty4Amount;
    }

    public void setDifficulty4Amount(Integer difficulty4Amount) {
        this.difficulty4Amount = difficulty4Amount;
    }

    public Integer getDifficulty5Amount() {
        return difficulty5Amount;
    }

    public void setDifficulty5Amount(Integer difficulty5Amount) {
        this.difficulty5Amount = difficulty5Amount;
    }

    public Integer getSpringSecurityAmount() {
        return springSecurityAmount;
    }

    public void setSpringSecurityAmount(Integer springSecurityAmount) {
        this.springSecurityAmount = springSecurityAmount;
    }
}