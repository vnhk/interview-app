package com.bervan.interviewapp.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSessionDetailDto {
    private UUID id;
    private String candidateName;
    private String configName;
    private String status;
    private Integer totalQuestions;
    private String mainTags;
    private String secondaryTags;
    private String notes;
    private String planTemplate;
    private String feedback;
    private String skillLevelConfig;
    private LocalDateTime modificationDate;
    private List<InterviewSessionQuestionDto> sessionQuestions;
    private List<InterviewSessionCodingTaskDto> sessionCodingTasks;
}
