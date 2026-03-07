package com.satyanand.quoraapp.services;

import com.satyanand.quoraapp.dto.QuestionRequestDTO;
import com.satyanand.quoraapp.dto.QuestionResponseDTO;
import com.satyanand.quoraapp.models.Question;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IQuestionService {

    Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO);
    Flux<QuestionResponseDTO> getAllQuestions();
    Mono<QuestionResponseDTO> getQuestionById(String id);
    Mono<Void> deleteQuestionById(String id);


}
