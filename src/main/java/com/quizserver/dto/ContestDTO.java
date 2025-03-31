package com.quizserver.dto;

import com.quizserver.enums.ContestType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContestDTO {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;
    private String testTitle;
    private String testDescription;
    private List<String> participants;

    @Getter
    @Setter
    private ContestType contestType;
    public void setTestDTO(TestDTO testDTO)
    {
        this.testTitle = testDTO.getTitle();
        this.testDescription = testDTO.getDescription();
    }

}
