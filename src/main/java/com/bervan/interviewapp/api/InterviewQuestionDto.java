package com.bervan.interviewapp.api;

import com.bervan.core.model.BaseDTO;
import com.bervan.core.model.BaseModel;
import com.bervan.interviewapp.interviewquestions.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionDto implements BaseDTO<UUID> {
    private UUID id;
    private String name;
    private String tags;
    private Integer difficulty;
    private String questionDetails;
    private String answerDetails;
    private Double maxPoints;

    @Override
    public Class<? extends BaseModel<UUID>> dtoTarget() {
        return Question.class;
    }
}
