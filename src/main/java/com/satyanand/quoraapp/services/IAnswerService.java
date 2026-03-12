package com.satyanand.quoraapp.services;

import com.satyanand.quoraapp.dto.AnswerRequestDTO;
import com.satyanand.quoraapp.dto.AnswerResponseDTO;
import com.satyanand.quoraapp.models.Answer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAnswerService {

    Mono<AnswerResponseDTO> createAnswer(AnswerRequestDTO request);
    Mono<AnswerResponseDTO> getAnswerById(String id);
    Mono<AnswerResponseDTO> updateAnswer(String id, AnswerRequestDTO answerRequestDTO);
    Mono<Void> deleteAnswer(String id);
    Flux<AnswerResponseDTO> getAllAnswers();
    Flux<AnswerResponseDTO> getAnswersByQuestionId(String questionId);
    Mono<Long> getAnswerCountByQuestionId(String questionId);
    Flux<AnswerResponseDTO> getAnswersByQuestionIdOrderByCreatedAtDesc(String questionId);
    Flux<AnswerResponseDTO> getAnswersByQuestionIdOrderByCreatedAtAsc(String questionId);
}
