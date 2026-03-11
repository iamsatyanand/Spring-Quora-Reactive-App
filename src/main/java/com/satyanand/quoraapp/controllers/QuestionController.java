package com.satyanand.quoraapp.controllers;

import com.satyanand.quoraapp.dto.CursorPageResponseDTO;
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
    public Flux<QuestionResponseDTO> getAllQuestions(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return questionService.getAllQuestions(cursor, size);
    }

    @GetMapping("/cursor")
    public Mono<CursorPageResponseDTO<QuestionResponseDTO>> getAllQuestionsWithCursor(
            @RequestParam(required = false) String prevCursor,
            @RequestParam(required = false) String nextCursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return questionService.getAllQuestionsWithCursorResponse(prevCursor, nextCursor, size);
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

    @GetMapping("/search")
    public Flux<QuestionResponseDTO> searchQuestions(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return questionService.searchQuestions(query, page, size);
    }

}