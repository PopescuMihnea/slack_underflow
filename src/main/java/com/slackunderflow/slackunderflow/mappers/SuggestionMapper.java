package com.slackunderflow.slackunderflow.mappers;

import com.slackunderflow.slackunderflow.dtos.requests.SuggestionRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.SuggestionResponseDto;
import com.slackunderflow.slackunderflow.errors.ModelNotFoundError;
import com.slackunderflow.slackunderflow.models.Suggestion;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.AnswerRepository;
import com.slackunderflow.slackunderflow.repositories.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

@Component
public class SuggestionMapper extends BodyEntityMapper<Suggestion, SuggestionResponseDto, SuggestionRequestDto> {

    private final AnswerRepository answerRepository;

    public SuggestionMapper(AnswerRepository answerRepository, UserMapper userMapper) {
        super(userMapper);
        this.answerRepository = answerRepository;
    }

    public SuggestionResponseDto fromEntityToResponse(Suggestion suggestion) {
        return SuggestionResponseDto.builder()
                .id(suggestion.getId())
                .body(suggestion.getBody())
                .createTimestamp(suggestion.getCreateTimestamp())
                .updateTimestamp(suggestion.getUpdateTimestamp())
                .answer(suggestion.getAnswer())
                .user(userMapper.fromEntityToResponseDto(suggestion.getUser(), null)).build();
    }

    public Suggestion fromRequestToEntity(SuggestionRequestDto suggestionRequestDto, UserEntity user) {
        var answer = answerRepository
                .findById(suggestionRequestDto.getAnswerId())
                .orElseThrow(() ->
                        new ModelNotFoundError("Answer not found with id: ", suggestionRequestDto.getAnswerId().toString()));

        return Suggestion.builder().body(suggestionRequestDto.getBody()).answer(answer).user(user).build();
    }
}
