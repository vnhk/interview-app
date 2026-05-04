package com.bervan.interviewapp.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSessionRequest {
    private String notes;
    private String status;
    private String feedback;
    private List<SessionQuestionUpdate> sessionQuestions;
    private List<SessionCodingTaskUpdate> sessionCodingTasks;
}
