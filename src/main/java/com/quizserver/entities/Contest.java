package com.quizserver.entities;


import com.quizserver.dto.ContestDTO;
import com.quizserver.enums.ContestType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime; // start time of the contest
    private LocalDateTime endTime; // end time of the contest

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private ContestType contestType;

    @OneToOne
    @JoinColumn(name = "test_id")
    private Test test;          // quiz test associated with this contest

    @ManyToMany
    @JoinTable(
            name = "contest_participants",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private List<User> participants;

    public ContestDTO getDTO(){
        ContestDTO contestDTO = new ContestDTO();
        contestDTO.setId(id);
        contestDTO.setStartTime(startTime);
        contestDTO.setEndTime(endTime);
        contestDTO.setActive(isActive);
        contestDTO.setTestDTO(test.getDTO());
        contestDTO.setParticipants(participants.stream()
                .map(User::getName)
                .collect(Collectors.toList()));

        return contestDTO;
    }
}

