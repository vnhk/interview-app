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
public class SessionCodingTaskUpdate {
    private UUID id;
    private Double score;
    private String notes;
}
