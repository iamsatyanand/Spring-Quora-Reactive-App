package com.satyanand.quoraapp.controllers;

import com.satyanand.quoraapp.dto.QuestionRequestDTO;
import com.satyanand.quoraapp.dto.QuestionResponseDTO;
import com.satyanand.quoraapp.services.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final IQuestionService questionService;

    @PostMapping
    public Mono<QuestionResponseDTO> createQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        return questionService.createQuestion(questionRequestDTO)
                .doOnSuccess(response -> System.out.println("Question created successfully in controller: " + response))
                .doOnError(error -> System.out.println("Error creating question in controller: " + error));
    }

    @GetMapping
    public Flux<QuestionResponseDTO> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public Mono<QuestionResponseDTO> getQuestionById(@PathVariable String id) {
        return questionService.getQuestionById(id)
                .doOnError(error -> System.out.println("Error fetching question by id: " + error));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteQuestionById(@PathVariable String id) {
        return questionService.deleteQuestionById(id)
                .doOnError(error -> System.out.println("Error deleting question by id: " + error));
    }
}