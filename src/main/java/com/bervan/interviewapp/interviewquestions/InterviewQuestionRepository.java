package com.bervan.interviewapp.interviewquestions;

import com.bervan.history.model.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface InterviewQuestionRepository extends BaseRepository<Question, UUID> {
    List<Question> findAllByDifficultyAndOwnersId(Integer difficulty, UUID ownerId);

    List<Question> findAllByOwnersId(UUID ownerId);
}
