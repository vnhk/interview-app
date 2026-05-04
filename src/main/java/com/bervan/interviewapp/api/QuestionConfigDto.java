package com.bervan.interviewapp.api;

import com.bervan.core.model.BaseDTO;
import com.bervan.core.model.BaseModel;
import com.bervan.interviewapp.questionconfig.QuestionConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionConfigDto implements BaseDTO<UUID> {
    private UUID id;
    private String name;
    private Integer difficulty1Percent;
    private Integer difficulty2Percent;
    private Integer difficulty3Percent;
    private Integer difficulty4Percent;
    private Integer difficulty5Percent;
    private Integer codingTasksAmount;

    @Override
    public Class<? extends BaseModel<UUID>> dtoTarget() {
        return QuestionConfig.class;
    }
}
