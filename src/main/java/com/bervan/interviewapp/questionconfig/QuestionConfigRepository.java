package com.bervan.interviewapp.questionconfig;

import com.bervan.history.model.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface QuestionConfigRepository extends BaseRepository<QuestionConfig, UUID> {
    Optional<QuestionConfig> findByName(String name);
}
