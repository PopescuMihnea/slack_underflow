package com.slackunderflow.slackunderflow.controller;

import com.slackunderflow.slackunderflow.advices.ModelAdvice;
import com.slackunderflow.slackunderflow.advices.UserAdvice;
import com.slackunderflow.slackunderflow.configuration.TestSecurityConfig;
import com.slackunderflow.slackunderflow.controllers.AnswerController;
import com.slackunderflow.slackunderflow.controllers.SuggestionController;
import com.slackunderflow.slackunderflow.dtos.responses.AnswerResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.SuggestionResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;
import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.models.*;
import com.slackunderflow.slackunderflow.services.implementation.AnswerServiceImpl;
import com.slackunderflow.slackunderflow.services.implementation.SuggestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SuggestionController.class)
@WebMvcTest(SuggestionController.class)
@Import({TestSecurityConfig.class, ModelAdvice.class, UserAdvice.class})
public class SuggestionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SuggestionServiceImpl suggestionService;

    private UserEntity user;

    private Question question;

    private Answer answer;
    private AnswerResponseDto answerResponseDto;

    private Suggestion suggestion;

    private SuggestionResponseDto suggestionResponseDto;

    private final String username = "Mihnea";
    private final String password = "12345";

    @BeforeEach
    public void init() {
        String authority = "USER";
        Role role = new Role(1L, authority);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Topic topic = Topic.builder().topic(TopicEnum.SCIENCE).build();
        Topic topic2 = Topic.builder().topic(TopicEnum.PHILOSOPHY).build();
        Set<Topic> topics = new HashSet<>();
        topics.add(topic);
        topics.add(topic2);


        user = UserEntity
                .builder()
                .id(1L)
                .badge(BadgeEnum.BEGINNER)
                .password("Hashed password")
                .points(0)
                .username(username)
                .authorities(roles).build();

        LocalDateTime timestamp = LocalDateTime.now();

        question = Question.builder()
                .id(1L)
                .user(user)
                .title("How can I write JUNIT tests in my java exam? :)")
                .body("Only serious answers")
                .topics(topics)
                .createTimestamp(timestamp)
                .updateTimestamp(timestamp)
                .build();

        answer = Answer.builder()
                .id(1L)
                .user(user)
                .body("Lorem ipsum")
                .rank(0)
                .createTimestamp(timestamp)
                .updateTimestamp(timestamp)
                .question(question)
                .build();

        suggestion = Suggestion.builder()
                .id(1L)
                .user(user)
                .body("I suggest that...")
                .createTimestamp(timestamp)
                .updateTimestamp(timestamp)
                .answer(answer)
                .build();

        UserResponseDto userResponseDto = new UserResponseDto(user.getUsername(), user.getPoints(), user.getBadge(), null);

        answerResponseDto = AnswerResponseDto.builder()
                .id(answer.getId())
                .user(userResponseDto)
                .body(answer.getBody())
                .rank(answer.getRank())
                .createTimestamp(answer.getCreateTimestamp())
                .updateTimestamp(answer.getUpdateTimestamp())
                .question(answer.getQuestion())
                .build();

        suggestionResponseDto = SuggestionResponseDto.builder()
                .id(suggestion.getId())
                .user(userResponseDto)
                .body(suggestion.getBody())
                .createTimestamp(suggestion.getCreateTimestamp())
                .updateTimestamp(suggestion.getUpdateTimestamp())
                .answer(answer)
                .build();
    }

    @DisplayName("JUnit test for get method")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenId_whenGet_thenReturnJson() throws Exception {
        var id = question.getId();

        given(suggestionService.get(id)).willReturn(suggestionResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/get/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(suggestion.getId()))
                .andExpect(jsonPath("$.body").value(suggestion.getBody()))
                .andExpect(jsonPath("$.answer.id").value(answer.getId()))
                .andExpect(jsonPath("$.answer.body").value(answer.getBody()))
                .andExpect(jsonPath("$.answer.rank").value(answer.getRank()))
                .andExpect(jsonPath("$.answer.user.username").value(user.getUsername()))
                .andExpect(jsonPath("$.answer.user.points").value(user.getPoints()))
                .andExpect(jsonPath("$.answer.user.badge").value(user.getBadge().toString()))
                /*.andExpect(jsonPath("$.createTimestamp").value(question.getCreateTimestamp()))
                .andExpect(jsonPath("$.updateTimestamp").value(question.getUpdateTimestamp()))*/
                .andExpect(jsonPath("$.user.username").value(user.getUsername()))
                .andExpect(jsonPath("$.user.points").value(user.getPoints()))
                .andExpect(jsonPath("$.user.badge").value(user.getBadge().toString()));

    }

    @DisplayName("JUnit test for get method that throws error from validation")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenId_whenGet_thenReturnError() throws Exception {
        var errorMessage = "must be greater than or equal to 1";

        mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/get/0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(errorMessage));

    }
}
