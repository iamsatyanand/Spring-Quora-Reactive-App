package com.satyanand.quoraapp.adapter;

import com.satyanand.quoraapp.dto.AnswerRequestDTO;
import com.satyanand.quoraapp.dto.AnswerResponseDTO;
import com.satyanand.quoraapp.dto.QuestionRequestDTO;
import com.satyanand.quoraapp.dto.QuestionResponseDTO;
import com.satyanand.quoraapp.models.Answer;
import com.satyanand.quoraapp.models.Question;

import java.time.LocalDateTime;

public class AnswerAdapter {

    public static AnswerResponseDTO toDTO(Answer answer) {
        return AnswerResponseDTO.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .build();
    }

    public static Answer toEntity(AnswerRequestDTO answerRequestDTO){
        return Answer.builder()
                .content(answerRequestDTO.getContent())
                .questionId(answerRequestDTO.getQuestionId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
