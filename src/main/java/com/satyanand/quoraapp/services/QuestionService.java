package com.satyanand.quoraapp.services;

import com.satyanand.quoraapp.adapter.QuestionAdapter;
import com.satyanand.quoraapp.dto.QuestionRequestDTO;
import com.satyanand.quoraapp.dto.QuestionResponseDTO;
import com.satyanand.quoraapp.models.Question;
import com.satyanand.quoraapp.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService{

    public final QuestionRepository questionRepository;
    @Override
    public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO) {

        Question question = QuestionAdapter.toEntity(questionRequestDTO);

        return questionRepository.save(question)
                .map(QuestionAdapter::toQuestionResponseDTO)
                .doOnSuccess(response -> System.out.println("Question created successfully "+ response))
                .doOnError(error -> System.out.println("Error creating question "+ error ));


    }

    @Override
    public Flux<QuestionResponseDTO> getAllQuestions() {
        return questionRepository.findAll()
                .map(QuestionAdapter::toQuestionResponseDTO);
    }

    @Override
    public Mono<QuestionResponseDTO> getQuestionById(String id) {
        return questionRepository.findById(id)
                .map(QuestionAdapter::toQuestionResponseDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Question not found with id: " + id)));

    }

    @Override
    public Mono<Void> deleteQuestionById(String id) {
        return questionRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Question not found with id: " + id)))
                .flatMap(question -> questionRepository.deleteById(question.getId()))
                .doOnSuccess(unused -> System.out.println("Question deleted successfully with id: " + id))
                .doOnError(error -> System.out.println("Error deleting question: " + error));


    }
}
