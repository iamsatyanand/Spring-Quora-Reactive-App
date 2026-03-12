package com.satyanand.quoraapp.repositories;

import com.satyanand.quoraapp.models.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface QuestionRepository extends ReactiveMongoRepository<Question, String> {

    Flux<Question> findByTitleContainingIgnoreCase(String title);

    @Query(" { $or : [ {title : { $options : 'i', $regex : ?0} }, {content : {$options : 'i', $regex : ?0}} ] } ")
    Flux<Question> findByTitleOrContentContainingIgnoreCase(String searchTerm, Pageable pageable);

    Flux<Question> findByCreatedAtGreaterThanOrderByCreatedAtAsc(LocalDateTime createdAt, Pageable pageable);

    Flux<Question> findByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime createdAt, Pageable pageable);

    Flux<Question> findAllByOrderByCreatedAtAsc(Pageable pageable);

    Flux<Question> findTop10ByOrderByCreatedAtAsc();


    // search via text search indexing and also sort via ranking
//    @Query(value = "{ $text: { $search: ?0 } }",
//            sort = "{ score: { $meta: 'textScore' } }")
//    Flux<Question> searchQuestionsByText(String query);

}
