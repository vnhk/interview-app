package com.bervan.interviewapp.interviewquestions;

import com.bervan.common.model.PersistableTableData;
import com.bervan.common.user.User;
import com.bervan.history.model.AbstractBaseHistoryEntity;
import com.bervan.history.model.HistoryField;
import com.bervan.history.model.HistoryOwnerEntity;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@HistorySupported
public class HistoryQuestion implements AbstractBaseHistoryEntity<UUID>, PersistableTableData<UUID>,
        ExcelIEEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;

    @HistoryField
    private String name;
    @HistoryField
    private String tags;
    @HistoryField
    private Integer difficulty;
    @HistoryField
    @Lob
    private String questionDetails;
    @HistoryField
    @Lob
    private String answerDetails;
    @HistoryField
    private Double maxPoints;
    private LocalDateTime modificationDate;

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
    private Question question;


    public HistoryQuestion(String name, String tags, int difficulty, String questionDetails, String answerDetails, double maxPoints) {
        this.name = name;
        this.tags = tags;
        this.difficulty = difficulty;
        this.questionDetails = questionDetails;
        this.answerDetails = answerDetails;
        this.maxPoints = maxPoints;
    }

    public HistoryQuestion() {

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
    public LocalDateTime getUpdateDate() {
        return modificationDate;
    }

    @Override
    public void setUpdateDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}