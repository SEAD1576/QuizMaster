package com.quizserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionsDTO {
    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
}
