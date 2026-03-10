package com.satyanand.quoraapp.services;

import com.satyanand.quoraapp.adapter.QuestionAdapter;
import com.satyanand.quoraapp.dto.CursorPageResponseDTO;
import com.satyanand.quoraapp.dto.QuestionRequestDTO;
import com.satyanand.quoraapp.dto.QuestionResponseDTO;
import com.satyanand.quoraapp.models.Question;
import com.satyanand.quoraapp.repositories.QuestionRepository;
import com.satyanand.quoraapp.utils.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

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
    public Mono<CursorPageResponseDTO<QuestionResponseDTO>> getAllQuestionsWithCursorResponse(String prevCursor, String nextCursor, int size) {

        int limit = size + 1;
        Pageable pageable = PageRequest.of(0, size);

        Flux<QuestionResponseDTO> questionFlux;
        Mono<List<QuestionResponseDTO>> questionListMono;
        if(!CursorUtils.isValidCursor(prevCursor) && !CursorUtils.isValidCursor(nextCursor)){
            return null;
        }
        else if(prevCursor == null || prevCursor.isBlank()){
            LocalDateTime nextCursorDate = CursorUtils.parseCursor(nextCursor);
            questionFlux = questionRepository
                    .findByCreatedAtGreaterThanOrderByCreatedAtAsc(nextCursorDate, pageable)
                    .map(QuestionAdapter::toQuestionResponseDTO);

            questionListMono = questionFlux.collectList();
            return questionListMono.map(questionList -> buildCursorResponse(questionList, limit));

        }
        else{

        }


    }

    private CursorPageResponseDTO<QuestionResponseDTO> buildCursorResponse(List<QuestionResponseDTO> questions, int size){

        boolean hasNext = size > questions.size();



        return null;
    }

    @Override
    public Flux<QuestionResponseDTO> getAllQuestions( String cursor, int size) {

        Pageable pageable = PageRequest.of(0, size);
        if(!CursorUtils.isValidCursor(cursor)){

            return questionRepository.findTop10ByOrderByCreatedAtAsc()
                    .take(size)
                    .map(QuestionAdapter::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error fetching questions "+ error))
                    .doOnComplete(() -> System.out.println("Fetched successfully"));
        }
        else{
            LocalDateTime cursorDateTime = CursorUtils.parseCursor(cursor);
            return questionRepository.findByCreatedAtGreaterThanOrderByCreatedAtAsc(cursorDateTime, pageable)
                    .map(QuestionAdapter::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error fetching questions "+ error))
                    .doOnComplete(() -> System.out.println("Fetched successfully"));
        }


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

    @Override
    public Flux<QuestionResponseDTO> searchQuestions(String query, int page, int size) {
        return questionRepository.findByTitleOrContentContainingIgnoreCase(query, PageRequest.of(page, size))
                .map(QuestionAdapter::toQuestionResponseDTO)
                .doOnError(error -> System.out.println("Error searching questions: " + error.getMessage()))
                .doOnComplete(() -> System.out.println("question found successfully"));
    }
}
