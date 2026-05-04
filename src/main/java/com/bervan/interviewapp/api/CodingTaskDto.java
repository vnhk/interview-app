package com.bervan.interviewapp.api;

import com.bervan.core.model.BaseDTO;
import com.bervan.core.model.BaseModel;
import com.bervan.interviewapp.codingtask.CodingTask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodingTaskDto implements BaseDTO<UUID> {
    private UUID id;
    private String name;
    private String initialCode;
    private String exampleCode;
    private String exampleCodeDetails;
    private String questions;

    @Override
    public Class<? extends BaseModel<UUID>> dtoTarget() {
        return CodingTask.class;
    }
}
