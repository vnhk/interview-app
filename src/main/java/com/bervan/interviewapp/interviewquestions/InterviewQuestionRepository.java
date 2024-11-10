package com.bervan.interviewapp.interviewquestions;

import com.bervan.history.model.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface InterviewQuestionRepository extends BaseRepository<Question, UUID> {
    List<Question> findAllByDifficultyAndTagsInAndOwnerId(Integer difficulty, List<String> tags, UUID ownerId);
}
