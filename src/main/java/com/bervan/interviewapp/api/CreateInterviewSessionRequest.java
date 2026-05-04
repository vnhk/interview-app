package com.bervan.interviewapp.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInterviewSessionRequest {
    private String candidateName;
    private String configName;
    private String mainTags;
    private String secondaryTags;
    private String skillLevelConfig;
    private List<UUID> questionIds;
    private List<UUID> codingTaskIds;
}
