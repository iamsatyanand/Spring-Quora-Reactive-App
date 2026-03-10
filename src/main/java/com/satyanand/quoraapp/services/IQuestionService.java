package com.satyanand.quoraapp.services;

import com.satyanand.quoraapp.dto.QuestionRequestDTO;
import com.satyanand.quoraapp.dto.QuestionResponseDTO;
import com.satyanand.quoraapp.models.Question;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IQuestionService {

    Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO);
    Flux<QuestionResponseDTO> getAllQuestionsWithCursorResponse(String prevCursor, String nextCursor, int size);
    Flux<QuestionResponseDTO> getAllQuestions(String Cursor, int size);
    Mono<QuestionResponseDTO> getQuestionById(String id);
    Mono<Void> deleteQuestionById(String id);
    Flux<QuestionResponseDTO> searchQuestions(String query, int page, int size);



}
