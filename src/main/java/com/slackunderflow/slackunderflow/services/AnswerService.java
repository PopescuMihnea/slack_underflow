package com.slackunderflow.slackunderflow.services;

import com.slackunderflow.slackunderflow.dtos.requests.AnswerRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.AnswerResponseDto;
import com.slackunderflow.slackunderflow.mappers.AnswerMapper;
import com.slackunderflow.slackunderflow.models.Answer;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.repositories.AnswerRepository;

import java.util.List;

public interface AnswerService
        extends BodyEntityService<Answer, AnswerResponseDto, AnswerRequestDto, AnswerRepository,
        AnswerMapper> {

    List<AnswerResponseDto> resetRanksByQuestion(Long questionId);

    List<AnswerResponseDto> updateRank(Long id, Integer rank, String username);

    List<AnswerResponseDto> getAllByQuestion(Long id);


    boolean deleteByQuestion(Question question);
}
