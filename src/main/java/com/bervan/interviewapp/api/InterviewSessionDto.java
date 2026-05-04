package com.bervan.interviewapp.api;

import com.bervan.core.model.BaseDTO;
import com.bervan.core.model.BaseModel;
import com.bervan.interviewapp.session.InterviewSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSessionDto implements BaseDTO<UUID> {
    private UUID id;
    private String candidateName;
    private String configName;
    private String status;
    private Integer totalQuestions;
    private String mainTags;
    private String secondaryTags;
    private LocalDateTime modificationDate;

    @Override
    public Class<? extends BaseModel<UUID>> dtoTarget() {
        return InterviewSession.class;
    }
}
