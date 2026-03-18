package com.satyanand.quoraapp.strategy;

import reactor.core.publisher.Mono;

public interface ViewCountIncrStrategy {

    Mono<Void> incrementViewCount(String targetId);

    boolean supports(String targetType);
}
