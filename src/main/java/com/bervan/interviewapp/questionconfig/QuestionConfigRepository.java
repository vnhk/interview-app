package com.bervan.interviewapp.questionconfig;

import com.bervan.history.model.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionConfigRepository extends BaseRepository<QuestionConfig, UUID> {
    List<QuestionConfig> findByNameAndOwnersId(String name, UUID ownerId);
}
