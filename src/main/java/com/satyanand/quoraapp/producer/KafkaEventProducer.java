package com.satyanand.quoraapp.producer;

import com.satyanand.quoraapp.events.ViewCountEvent;
import com.satyanand.quoraapp.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishViewCountEvent(ViewCountEvent viewCountEvent){
        kafkaTemplate.send(KafkaConfig.TOPIC_NAME, viewCountEvent.getTargetId(), viewCountEvent)
                .whenComplete((result, error) -> {
                    if(error != null){
                        System.out.println("Error publishing event: " + error.getMessage());
                    } else {
                        System.out.println("Event published successfully to topic: " + KafkaConfig.TOPIC_NAME);
                    }
                });
    }

}
