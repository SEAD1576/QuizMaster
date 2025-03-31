package com.quizserver.service.contest;

import com.quizserver.dto.ContestDTO;
import com.quizserver.entities.Contest;

public interface ContestService {

    ContestDTO createContest(ContestDTO contestDTO);  // Create contest

    ContestDTO getContestById(Long contestId);     // Fetch contest by ID

    String registerUserForContest(Long contestId, Long userId);
}
