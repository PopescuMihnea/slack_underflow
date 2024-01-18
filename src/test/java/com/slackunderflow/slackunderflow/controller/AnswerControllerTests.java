package com.slackunderflow.slackunderflow.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.slackunderflow.slackunderflow.advices.ModelAdvice;
import com.slackunderflow.slackunderflow.advices.UserAdvice;
import com.slackunderflow.slackunderflow.configuration.TestSecurityConfig;
import com.slackunderflow.slackunderflow.controllers.AnswerController;
import com.slackunderflow.slackunderflow.controllers.UserController;
import com.slackunderflow.slackunderflow.dtos.responses.AnswerResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.QuestionResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;
import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.models.*;
import com.slackunderflow.slackunderflow.services.implementation.AnswerServiceImpl;
import com.slackunderflow.slackunderflow.services.implementation.UserEntityServiceImpl;
import org.junit.jupiter.api.BeforeAll;
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
@ContextConfiguration(classes = AnswerController.class)
@WebMvcTest(AnswerController.class)
@Import({TestSecurityConfig.class, ModelAdvice.class, UserAdvice.class})
public class AnswerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnswerServiceImpl answerService;

    private UserEntity user;

    private Question question;

    private Answer answer;
    private AnswerResponseDto answerResponseDto;

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
    }

    @DisplayName("JUnit test for get method")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenId_whenGet_thenReturnJson() throws Exception {
        var id = question.getId();

        given(answerService.get(id)).willReturn(answerResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/answer/get/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(answer.getId()))
                .andExpect(jsonPath("$.body").value(answer.getBody()))
                .andExpect(jsonPath("$.rank").value(answer.getRank()))
                .andExpect(jsonPath("$.question.id").value(question.getId()))
                .andExpect(jsonPath("$.question.body").value(question.getBody()))
                .andExpect(jsonPath("$.question.title").value(question.getTitle()))
                .andExpect(jsonPath("$.question.topics[0]").value(question.getTopics().toArray()[0]))
                .andExpect(jsonPath("$.question.topics[1]").value(question.getTopics().toArray()[1]))
                .andExpect(jsonPath("$.question.user.username").value(user.getUsername()))
                .andExpect(jsonPath("$.question.user.points").value(user.getPoints()))
                .andExpect(jsonPath("$.question.user.badge").value(user.getBadge().toString()))
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

        mockMvc.perform(MockMvcRequestBuilders.get("/answer/get/0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(errorMessage));

    }
}
