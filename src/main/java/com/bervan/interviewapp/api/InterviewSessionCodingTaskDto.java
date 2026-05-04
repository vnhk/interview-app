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
public class InterviewSessionCodingTaskDto {
    private UUID id;
    private Integer taskNumber;
    private Double score;
    private String notes;
    private UUID codingTaskId;
    private String codingTaskName;
    private String initialCode;
    private String exampleCode;
    private String exampleCodeDetails;
    private String taskQuestions;
}
