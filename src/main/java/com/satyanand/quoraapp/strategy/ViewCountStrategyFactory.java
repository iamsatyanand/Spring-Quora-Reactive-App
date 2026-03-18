package com.satyanand.quoraapp.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewCountStrategyFactory {

    private final List<ViewCountIncrStrategy> strategies;

    public ViewCountIncrStrategy getStrategy(String targetType){
        return strategies.stream()
                .filter(strategy -> strategy.supports(targetType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for type: " + targetType));
    }

}
