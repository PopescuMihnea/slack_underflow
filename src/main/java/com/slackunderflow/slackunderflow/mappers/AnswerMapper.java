package com.slackunderflow.slackunderflow.mappers;

import com.slackunderflow.slackunderflow.dtos.requests.AnswerRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.AnswerResponseDto;
import com.slackunderflow.slackunderflow.errors.ModelNotFoundError;
import com.slackunderflow.slackunderflow.models.Answer;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper extends BodyEntityMapper<Answer, AnswerResponseDto, AnswerRequestDto> {

    private final QuestionRepository questionRepository;

    public AnswerMapper(QuestionRepository questionRepository, UserMapper userMapper) {
        super(userMapper);
        this.questionRepository = questionRepository;
    }

    public AnswerResponseDto fromEntityToResponse(Answer answer) {
        return AnswerResponseDto.builder()
                .id(answer.getId())
                .body(answer.getBody())
                .rank(answer.getRank())
                .createTimestamp(answer.getCreateTimestamp())
                .updateTimestamp(answer.getUpdateTimestamp())
                .question(answer.getQuestion())
                .user(userMapper.fromEntityToResponseDto(answer.getUser(), null)).build();

    }

    public Answer fromRequestToEntity(AnswerRequestDto answerRequestDto, UserEntity user) {
        var question = questionRepository.findById(answerRequestDto
                        .getQuestionId())
                .orElseThrow(() ->
                        new ModelNotFoundError("Question not found with id: ", answerRequestDto.getQuestionId().toString()));

        return Answer.builder().body(answerRequestDto.getBody()).question(question).user(user).build();
    }
}
