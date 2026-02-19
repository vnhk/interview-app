package com.bervan.interviewapp.questionconfig;

import com.bervan.common.model.BervanOwnedBaseEntity;
import com.bervan.common.model.PersistableTableOwnedData;
import com.bervan.history.model.HistoryCollection;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@HistorySupported
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "owner.id"})
)
public class QuestionConfig extends BervanOwnedBaseEntity<UUID> implements PersistableTableOwnedData<UUID>, ExcelIEEntity<UUID> {
    private String name;
    private Integer difficulty1Percent;
    private Integer difficulty2Percent;
    private Integer difficulty3Percent;
    private Integer difficulty4Percent;
    private Integer difficulty5Percent;
    private Integer codingTasksAmount;
    private LocalDateTime modificationDate;
    private boolean deleted;
    @Id
    private UUID id;

    @OneToMany(fetch = FetchType.EAGER)
    @HistoryCollection(historyClass = HistoryQuestionConfig.class)
    private Set<HistoryQuestionConfig> history = new HashSet<>();

    @Override
    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public QuestionConfig() {

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

    public Integer getCodingTasksAmount() {
        return codingTasksAmount;
    }

    public void setCodingTasksAmount(Integer codingTasksAmount) {
        this.codingTasksAmount = codingTasksAmount;
    }
}
