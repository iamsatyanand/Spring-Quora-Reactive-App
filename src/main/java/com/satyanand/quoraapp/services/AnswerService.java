package com.satyanand.quoraapp.services;

import com.satyanand.quoraapp.adapter.AnswerAdapter;
import com.satyanand.quoraapp.adapter.QuestionAdapter;
import com.satyanand.quoraapp.dto.AnswerRequestDTO;
import com.satyanand.quoraapp.dto.AnswerResponseDTO;
import com.satyanand.quoraapp.repositories.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnswerService implements IAnswerService{

    private final AnswerRepository answerRepository;

    @Override
    public Mono<AnswerResponseDTO> createAnswer(AnswerRequestDTO request) {
        return answerRepository.save(AnswerAdapter.toEntity(request))
                .map(AnswerAdapter::toDTO)
                .doOnSuccess(response -> System.out.println("Answer created successfully "+ response))
                .doOnError(error -> System.out.println("Error creating answer "+ error ));
    }

    @Override
    public Mono<AnswerResponseDTO> getAnswerById(String id) {
        return answerRepository.findById(id)
                .map(AnswerAdapter::toDTO)
                .doOnSuccess(response -> System.out.println("Answer fetched successfully by id: "+ response))
                .doOnError(error -> System.out.println("Error fetching answer by id: "+ error ));
    }

    @Override
    public Mono<AnswerResponseDTO> updateAnswer(String id, AnswerRequestDTO answerRequestDTO) {
        return answerRepository.findById(id)
                .flatMap(answer -> {
                    answer.setContent(answerRequestDTO.getContent());
                    return answerRepository.save(answer);
                })
                .map(AnswerAdapter::toDTO)
                .doOnSuccess(response -> System.out.println("Answer updated successfully: "+ response))
                .doOnError(error -> System.out.println("Error updating answer: "+ error ));
    }

    @Override
    public Mono<Void> deleteAnswer(String id) {
        return answerRepository.deleteById(id)
                .doOnSuccess(response -> System.out.println("Answer deleted successfully with id: "+ id))
                .doOnError(error -> System.out.println("Error deleting answer with id: "+ id + " Error: " + error));
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
