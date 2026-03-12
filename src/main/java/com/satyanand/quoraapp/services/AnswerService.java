package com.satyanand.quoraapp.services;

import com.satyanand.quoraapp.dto.AnswerRequestDTO;
import com.satyanand.quoraapp.dto.AnswerResponseDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AnswerService implements IAnswerService{
    @Override
    public Mono<AnswerResponseDTO> createAnswer(AnswerRequestDTO request) {
        return null;
    }

    @Override
    public Mono<AnswerResponseDTO> getAnswerById(String id) {
        return null;
    }

    @Override
    public Mono<AnswerResponseDTO> updateAnswer(String id, AnswerRequestDTO answerRequestDTO) {
        return null;
    }

    @Override
    public Mono<Void> deleteAnswer(String id) {
        return null;
    }

    @Override
    public Flux<AnswerResponseDTO> getAllAnswers() {
        return null;
    }

    @Override
    public Flux<AnswerResponseDTO> getAnswersByQuestionId(String questionId) {
        return null;
    }

    @Override
    public Mono<Long> getAnswerCountByQuestionId(String questionId) {
        return null;
    }

    @Override
    public Flux<AnswerResponseDTO> getAnswersByQuestionIdOrderByCreatedAtDesc(String questionId) {
        return null;
    }

    @Override
    public Flux<AnswerResponseDTO> getAnswersByQuestionIdOrderByCreatedAtAsc(String questionId) {
        return null;
    }
}
