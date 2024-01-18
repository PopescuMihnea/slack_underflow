package com.slackunderflow.slackunderflow.service;

import com.slackunderflow.slackunderflow.advices.ModelAdvice;
import com.slackunderflow.slackunderflow.advices.UserAdvice;
import com.slackunderflow.slackunderflow.dtos.responses.AnswerResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.QuestionResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;
import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.errors.ModelNotFoundError;
import com.slackunderflow.slackunderflow.mappers.AnswerMapper;
import com.slackunderflow.slackunderflow.mappers.QuestionMapper;
import com.slackunderflow.slackunderflow.models.*;
import com.slackunderflow.slackunderflow.repositories.AnswerRepository;
import com.slackunderflow.slackunderflow.repositories.QuestionRepository;
import com.slackunderflow.slackunderflow.services.implementation.AnswerServiceImpl;
import com.slackunderflow.slackunderflow.services.implementation.QuestionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Import({ModelAdvice.class, UserAdvice.class})
public class AnswerServiceTests {
    @Mock
    private AnswerMapper answerMapper;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerServiceImpl answerService;

    private Long id = 1L;

    @DisplayName("JUnit test for get method")
    @Test
    public void givenId_whenGetAnswer_thenReturnJson() {
        var username = "Mihnea";

        String authority = "USER";
        var role = new Role(1L, authority);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        var topic = Topic.builder().topic(TopicEnum.SCIENCE).build();
        Set<Topic> topics = new HashSet<>();
        topics.add(topic);

        Set<TopicEnum> topicEnums = new HashSet<>();
        topicEnums.add(TopicEnum.SCIENCE);

        var user = UserEntity
                .builder()
                .id(id)
                .badge(BadgeEnum.BEGINNER)
                .password("Hashed password")
                .points(0)
                .username(username)
                .authorities(roles).build();

        LocalDateTime timestamp = LocalDateTime.now();

        var question = Question.builder()
                .id(1L)
                .user(user)
                .title("How can I write JUNIT tests in my java exam? :)")
                .body("Only serious answers")
                .topics(topics)
                .createTimestamp(timestamp)
                .updateTimestamp(timestamp)
                .build();

        var answer = Answer.builder()
                .id(1L)
                .user(user)
                .body("Lorem ipsum")
                .rank(0)
                .createTimestamp(timestamp)
                .updateTimestamp(timestamp)
                .question(question)
                .build();

        var userResponseDto = new UserResponseDto(user.getUsername(), user.getPoints(), user.getBadge(), null);

        var answerResponseDto = AnswerResponseDto.builder()
                .id(answer.getId())
                .user(userResponseDto)
                .body(answer.getBody())
                .rank(answer.getRank())
                .createTimestamp(answer.getCreateTimestamp())
                .updateTimestamp(answer.getUpdateTimestamp())
                .question(answer.getQuestion())
                .build();

        given(answerRepository.findById(id)).willReturn(Optional.of(answer));

        given(answerMapper.fromEntityToResponse(answer)).willReturn(answerResponseDto);

        var foundAnswer = answerService.get(id);
        System.out.println("Test1 Answer result: " + foundAnswer.toString());

        assertThat(foundAnswer).isEqualTo(answerResponseDto);
    }

    @DisplayName("JUnit test for get method that throws error")
    @Test
    public void givenId_whenAnswer_thenReturnError() {
        String expectedMessage = "Entity not found with id: ";

        given(answerRepository.findById(id)).willReturn(Optional.empty());

        ModelNotFoundError ex = Assertions.assertThrows(ModelNotFoundError.class, () -> answerService.get(id));
        System.out.println("Test1 error Answer result: " + ex.toString());

        assertThat(ex.getMessage()).isEqualTo(expectedMessage);
        assertThat(ex.getBody()).isEqualTo(id.toString());
    }
}
