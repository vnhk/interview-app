package com.bervan.interviewapp.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSessionQuestionDto {
    private UUID id;
    private Integer questionNumber;
    private Double score;
    private String notes;
    private String answerStatus;
    private UUID questionId;
    private String questionName;
    private String questionTags;
    private Integer questionDifficulty;
    private String questionDetails;
    private String answerDetails;
    private Double maxPoints;
}
