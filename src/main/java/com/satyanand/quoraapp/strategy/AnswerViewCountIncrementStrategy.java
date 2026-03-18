package com.satyanand.quoraapp.strategy;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AnswerViewCountIncrementStrategy implements ViewCountIncrStrategy{
    @Override
    public Mono<Void> incrementViewCount(String targetId) {
        return null;
    }

    @Override
    public boolean supports(String targetType) {
        return "answer".equalsIgnoreCase(targetType);
    }
}
