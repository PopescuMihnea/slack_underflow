package com.slackunderflow.slackunderflow.services;

import com.slackunderflow.slackunderflow.dtos.requests.QuestionRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.QuestionResponseDto;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.mappers.QuestionMapper;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.QuestionRepository;

import java.util.List;

public interface QuestionService
        extends BodyEntityService<Question, QuestionResponseDto, QuestionRequestDto,
        QuestionRepository, QuestionMapper> {

    List<QuestionResponseDto> getAllByTopics(List<TopicEnum> topics);

    List<QuestionResponseDto> getAllByTitle(String title);

    boolean deleteByUser(UserEntity user);
}
