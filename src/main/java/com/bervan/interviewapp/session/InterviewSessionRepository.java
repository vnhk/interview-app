package com.bervan.interviewapp.session;

import com.bervan.history.model.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface InterviewSessionRepository extends BaseRepository<InterviewSession, UUID> {
    List<InterviewSession> findAllByOwnersId(UUID ownerId);
}
