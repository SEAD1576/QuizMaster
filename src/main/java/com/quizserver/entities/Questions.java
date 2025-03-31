package com.quizserver.entities;

import com.quizserver.dto.QuestionsDTO;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctOption;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    public QuestionsDTO getDTO(){
        QuestionsDTO dto = new QuestionsDTO();
        dto.setId(id);
        dto.setQuestionText(questionText);
        dto.setOptionA(optionA);
        dto.setOptionB(optionB);
        dto.setOptionC(optionC);
        dto.setOptionD(optionD);
        dto.setCorrectOption(correctOption);

        return dto;
    }
}
