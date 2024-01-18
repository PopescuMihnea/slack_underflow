package com.slackunderflow.slackunderflow.services;

import com.slackunderflow.slackunderflow.dtos.requests.SuggestionRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.SuggestionResponseDto;
import com.slackunderflow.slackunderflow.mappers.SuggestionMapper;
import com.slackunderflow.slackunderflow.models.Answer;
import com.slackunderflow.slackunderflow.models.Suggestion;
import com.slackunderflow.slackunderflow.repositories.SuggestionRepository;

import java.util.List;

public interface SuggestionService
        extends BodyEntityService<Suggestion, SuggestionResponseDto, SuggestionRequestDto,
        SuggestionRepository, SuggestionMapper> {


    List<SuggestionResponseDto> getAllByAnswer(Long id);


    boolean deleteByAnswer(Answer answer);
}
