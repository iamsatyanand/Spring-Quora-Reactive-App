package com.satyanand.quoraapp.services;

import com.satyanand.quoraapp.adapter.QuestionAdapter;
import com.satyanand.quoraapp.dto.CursorPageResponseDTO;
import com.satyanand.quoraapp.dto.CursorPaginationDTO;
import com.satyanand.quoraapp.dto.QuestionRequestDTO;
import com.satyanand.quoraapp.dto.QuestionResponseDTO;
import com.satyanand.quoraapp.models.Question;
import com.satyanand.quoraapp.repositories.QuestionRepository;
import com.satyanand.quoraapp.utils.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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


    // API contract:
    // Client must send only one cursor at a time.
    //
    // 1) First page request:
    //     prevCursor = null
    //     nextCursor = null
    //
    // 2️) Next page request:
    //     nextCursor will be provided
    //     prevCursor must be null
    //
    // 3️) Previous page request:
    //     prevCursor will be provided
    //     nextCursor must be null
    //
    // Sending both cursors in the same request is considered invalid.
    //    First page:
    //
    //    GET /questions?size=10
    //
    //    Next page:
    //
    //    GET /questions?nextCursor=abc123&size=10
    //
    //    Previous page:
    //
    //    GET /questions?prevCursor=xyz456&size=10
    //
    //    Invalid request:
    //
    //    GET /questions?nextCursor=abc&prevCursor=xyz
    @Override
    public Mono<CursorPageResponseDTO<QuestionResponseDTO>> getAllQuestionsWithCursorResponse(String prevCursor, String nextCursor, int size) {

        if (CursorUtils.isValidCursor(prevCursor) && CursorUtils.isValidCursor(nextCursor)) {
            return Mono.error(new IllegalArgumentException(
                    "Only one cursor (prevCursor or nextCursor) can be provided"));
        }
        int limit = size + 1;
        Pageable pageable = PageRequest.of(0, limit);

        Flux<QuestionResponseDTO> questionFlux;
        Mono<List<QuestionResponseDTO>> questionListMono;
        boolean isPrev = CursorUtils.isValidCursor(prevCursor);
        if(!CursorUtils.isValidCursor(prevCursor) && !CursorUtils.isValidCursor(nextCursor)){
            questionFlux = questionRepository
                    .findAllByOrderByCreatedAtAsc(pageable)
                    .map(QuestionAdapter::toQuestionResponseDTO);



        }
        else if(!CursorUtils.isValidCursor(prevCursor)){
            LocalDateTime nextCursorDate = CursorUtils.decode(nextCursor);
            questionFlux = questionRepository
                    .findByCreatedAtGreaterThanOrderByCreatedAtAsc(nextCursorDate, pageable)
                    .map(QuestionAdapter::toQuestionResponseDTO);


        }
        else{
            LocalDateTime prevCursorDate = CursorUtils.decode(prevCursor);
            questionFlux = questionRepository
                    .findByCreatedAtLessThanOrderByCreatedAtDesc(prevCursorDate, pageable)
                    .map(QuestionAdapter::toQuestionResponseDTO);


        }

        questionListMono = questionFlux.collectList();
        return questionListMono.map(questionList -> buildCursorResponse(questionList, size, isPrev));


    }

    private CursorPageResponseDTO<QuestionResponseDTO> buildCursorResponse(List<QuestionResponseDTO> questions, int size, boolean isPrev){

        boolean hasNext = questions.size() > size;

        if(hasNext) questions.remove(size);

        if (isPrev) {
            Collections.reverse(questions);
        }


        String nextCursor = null;
        String prevCursor = null;

        if(!questions.isEmpty()){
            QuestionResponseDTO lastQuestion = questions.get(questions.size() - 1);
            QuestionResponseDTO firstQuestion = questions.get(0);

            nextCursor = CursorUtils.encode(lastQuestion.getCreatedAt());
            prevCursor = CursorUtils.encode(firstQuestion.getCreatedAt());
        }

        return CursorPageResponseDTO.<QuestionResponseDTO>builder()
                .data(questions)
                .pagination(
                        CursorPaginationDTO.builder()
                                .hasPrev(prevCursor != null)
                                .hasNext(hasNext)
                                .nextCursor(nextCursor)
                                .prevCursor(prevCursor)
                                .size(questions.size())
                                .build()
                ).build();
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
