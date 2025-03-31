package com.quizserver.service.contest;

import com.quizserver.dto.ContestDTO;
import com.quizserver.entities.Contest;
import com.quizserver.entities.User;
import com.quizserver.enums.ContestType;
import com.quizserver.repository.ContestRepository;
import com.quizserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ContestDTO createContest(ContestDTO contestDTO) {
        Contest contest = new Contest();

        contest.setStartTime(contestDTO.getStartTime());
        contest.setEndTime(contestDTO.getEndTime());
        contest.setActive(contestDTO.isActive());
        contest.setContestType(contestDTO.getContestType());

        contestRepository.save(contest);

        ContestDTO createdContestDTO = new ContestDTO();
        createdContestDTO.setId(contest.getId());
        createdContestDTO.setStartTime(contest.getStartTime());
        createdContestDTO.setEndTime(contest.getEndTime());
        createdContestDTO.setActive(contest.isActive());
        createdContestDTO.setContestType(contest.getContestType());

        return createdContestDTO;
    }
    @Override
    public ContestDTO getContestById(Long contestId) {
        Optional<Contest> contestOpt = contestRepository.findById(contestId);
        if (contestOpt.isPresent()) {
            Contest contest = contestOpt.get();
            ContestDTO contestDTO = new ContestDTO();
            contestDTO.setId(contest.getId());
            contestDTO.setStartTime(contest.getStartTime());
            contestDTO.setEndTime(contest.getEndTime());
            contestDTO.setActive(contest.isActive());
            contestDTO.setContestType(contest.getContestType());
            return contestDTO;
        }
        return null;
    }

    @Override
    public String registerUserForContest(Long contestId, Long userId) {
        Optional<Contest> contestOpt = contestRepository.findById(contestId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (contestOpt.isPresent() && userOpt.isPresent()) {
            Contest contest = contestOpt.get();
            User user = userOpt.get();

            if (contest.getContestType() == ContestType.PUBLIC) {
                contest.getParticipants().add(user);
                contestRepository.save(contest);
                return "User registered successfully for public contest.";
            } else if (contest.getContestType() == ContestType.PRIVATE) {
                return "Private contest: Only admin can register participants.";
            }
        }
        return "Contest or User not found!";
    }
}
