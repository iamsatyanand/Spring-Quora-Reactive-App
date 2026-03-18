package com.satyanand.quoraapp.strategy;

import com.satyanand.quoraapp.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuestionViewCountIncrementStrategy implements ViewCountIncrStrategy{

    private final QuestionRepository questionRepository;

    @Override
    public Mono<Void> incrementViewCount(String targetId) {
        questionRepository.findById(targetId)
                .switchIfEmpty(Mono.error(new RuntimeException("Question not found with id: " + targetId)))
                .flatMap(question -> {
                    int views = question.getViews() == null ? 0 : question.getViews();
                    question.setViews(views + 1);
                    return questionRepository.save(question);
                }).then();
        return null;
    }

    @Override
    public boolean supports(String targetType) {
        return "question".equalsIgnoreCase(targetType);
    }
}
