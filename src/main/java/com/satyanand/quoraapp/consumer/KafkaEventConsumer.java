package com.satyanand.quoraapp.consumer;

import com.satyanand.quoraapp.events.ViewCountEvent;
import com.satyanand.quoraapp.config.KafkaConfig;
import com.satyanand.quoraapp.repositories.QuestionRepository;
import com.satyanand.quoraapp.strategy.ViewCountStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final QuestionRepository questionRepository;
    private final ViewCountStrategyFactory strategyFactory;

    @KafkaListener(topics = KafkaConfig.TOPIC_NAME, groupId = "view-count-consumer", containerFactory = "kafkaListenerContainerFactory")
    public void consumeViewCountEvent(ViewCountEvent viewCountEvent){
        questionRepository.findById(viewCountEvent.getTargetId())
                .switchIfEmpty(Mono.error(new RuntimeException("Question not found with id: " + viewCountEvent.getTargetId())))
                .flatMap(question -> {
                    int views = question.getViews() != null ? question.getViews() : 0;
                    question.setViews(views + 1);
                    return questionRepository.save(question);
                })
                .subscribe(question -> {
                    System.out.println("Updated view count for question id: " + question.getId() + " New view count: " + question.getViews());
                }, error -> {
                    System.out.println("Error processing view count event: " + error.getMessage());
                });
    }

    public void consumeViewCountEventByStrategy(ViewCountEvent viewCountEvent){
        strategyFactory.getStrategy(viewCountEvent.getTargetType())
                .incrementViewCount(viewCountEvent.getTargetId())
                .subscribe(null,error -> System.out.println("Error processing view count event: " + error.getMessage()) );
    }

}
