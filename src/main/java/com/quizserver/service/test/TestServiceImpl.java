package com.quizserver.service.test;


import com.quizserver.dto.*;
import com.quizserver.entities.Questions;
import com.quizserver.entities.Test;
import com.quizserver.entities.TestResult;
import com.quizserver.entities.User;
import com.quizserver.repository.QuestionRepository;
import com.quizserver.repository.TestRepository;
import com.quizserver.repository.TestResultRepository;
import com.quizserver.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService

{

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private UserRepository userRepository;

    public TestDTO createTest(TestDTO dto){
        Test test = new Test();

        test.setTitle(dto.getTitle());
        test.setDescription(dto.getDescription());
        test.setTime(dto.getTime());

        return testRepository.save(test).getDTO();
    }
    @Override
    public QuestionsDTO addQuestionInTest(QuestionsDTO dto){
        Optional<Test> optionalTest = testRepository.findById(dto.getId());
        if(optionalTest.isPresent()){
            Questions question = new Questions();

            question.setTest(optionalTest.get());
            question.setQuestionText(dto.getQuestionText());
            question.setOptionA(dto.getOptionA());
            question.setOptionB(dto.getOptionB());
            question.setOptionC(dto.getOptionC());
            question.setOptionD(dto.getOptionD());
            question.setCorrectOption(dto.getCorrectOption());
            return questionRepository.save(question).getDTO();
        }
        throw new EntityNotFoundException("Test Not Found");

    }
    @Override
    public List<TestDTO> getAllTests() {
        return testRepository.findAll()
                .stream()
                .map(test -> {
                    TestDTO dto = test.getDTO();
                    dto.setTime(test.getQuestions().size() * dto.getTime()); // Modify DTO, not entity
                    return dto;
                })
                .collect(Collectors.toList());
    }
    public TestDetailsDTO getAllQuestionsByTest(Long id){
        Optional<Test> optionalTest = testRepository.findById(id);
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        if(optionalTest.isPresent()){
            TestDTO testDTO = optionalTest.get().getDTO();
            testDTO.setTime(optionalTest.get().getTime() * optionalTest.get().getQuestions().size());
            testDetailsDTO.setTestDTO(testDTO);
            testDetailsDTO.setQuestions(optionalTest.get().getQuestions().stream().map(Questions::getDTO).toList());
            return testDetailsDTO;
        }
        return testDetailsDTO;
    }

    public TestResultDTO submitTest(SubmitTestDTO request) {
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new EntityNotFoundException("Test Not Found!"));

        User user = userRepository.findById(request.getUserId()) // changed getTestId() to getUserId()
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        int correctAnswers = 0;
        for (QuestionResponse response : request.getResponses()) {
            Questions question = questionRepository.findById(response.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question Not Found"));

            System.out.println("Correct Option: " + question.getCorrectOption());
            System.out.println("Selected Option: " + response.getSelectedOption());
            if (question.getCorrectOption().trim().equalsIgnoreCase(response.getSelectedOption().trim())) {
                correctAnswers++;
            }

            System.out.println("Correct Answers: " + correctAnswers);
        }

        int totalQuestions = test.getQuestions().size();
        double percentage = ((double) correctAnswers / totalQuestions) * 100;  // Cast one value to double

        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setUser(user);
        testResult.setTotalQuestions(totalQuestions);
        testResult.setCorrectAnswers(correctAnswers);
        testResult.setPercentage(percentage);

        return testResultRepository.save(testResult).getDto();
    }

    public List<TestResultDTO> getAllTestResults(){
        return testResultRepository.findAll().stream().map(TestResult:: getDto).collect(Collectors.toList());
    }

    public List<TestResultDTO> getAllTestResultsOfUser(Long userId){
        return testResultRepository.findAllByUserId(userId).stream().map(TestResult::getDto).collect(Collectors.toList());


    }

}
