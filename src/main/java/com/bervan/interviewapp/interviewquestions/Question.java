package com.bervan.interviewapp.interviewquestions;

import com.bervan.common.model.BervanBaseEntity;
import com.bervan.common.model.PersistableTableData;
import com.bervan.common.model.VaadinBervanColumn;
import com.bervan.history.model.HistoryCollection;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@HistorySupported
public class Question extends BervanBaseEntity<UUID> implements PersistableTableData<UUID>, ExcelIEEntity<UUID> {
    @Id
    private UUID id;
    @VaadinBervanColumn(displayName = "Name", internalName = "name")
    private String name;
    @VaadinBervanColumn(displayName = "Tags", internalName = "tags", strValues = {"Java/DB/Testing", "Security", "Frameworks"})
    private String tags;
    @Min(1)
    @Max(5)
    @VaadinBervanColumn(displayName = "Difficulty", internalName = "difficulty", intValues = {1, 2, 3, 4, 5})
    private Integer difficulty;
    @Lob
    @VaadinBervanColumn(displayName = "Question Details", internalName = "questionDetails")
    private String questionDetails;
    @Lob
    @VaadinBervanColumn(displayName = "Answer Details", internalName = "answerDetails")
    private String answerDetails;
    @VaadinBervanColumn(displayName = "Max Points", internalName = "maxPoints")
    private Double maxPoints;
    private LocalDateTime modificationDate;

    @OneToMany(fetch = FetchType.EAGER)
    @HistoryCollection(historyClass = HistoryQuestion.class)
    private Set<HistoryQuestion> history = new HashSet<>();

    private boolean deleted;

    public Question(String name, String tags, int difficulty, String questionDetails, String answerDetails, double maxPoints) {
        this.name = name;
        this.tags = tags;
        this.difficulty = difficulty;
        this.questionDetails = questionDetails;
        this.answerDetails = answerDetails;
        this.maxPoints = maxPoints;
    }

    public Question() {

    }

    public String getTableFilterableColumnValue() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(String questionDetails) {
        this.questionDetails = questionDetails;
    }

    public String getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(String answerDetails) {
        this.answerDetails = answerDetails;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
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

    public Set<HistoryQuestion> getHistory() {
        return history;
    }

    public void setHistory(Set<HistoryQuestion> history) {
        this.history = history;
    }

    @Override
    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}