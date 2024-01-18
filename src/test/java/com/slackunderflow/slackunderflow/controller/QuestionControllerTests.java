package com.slackunderflow.slackunderflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.slackunderflow.slackunderflow.advices.ModelAdvice;
import com.slackunderflow.slackunderflow.advices.UserAdvice;
import com.slackunderflow.slackunderflow.configuration.TestSecurityConfig;
import com.slackunderflow.slackunderflow.controllers.AnswerController;
import com.slackunderflow.slackunderflow.controllers.QuestionController;
import com.slackunderflow.slackunderflow.dtos.responses.QuestionResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;
import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.Role;
import com.slackunderflow.slackunderflow.models.Topic;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.services.implementation.AnswerServiceImpl;
import com.slackunderflow.slackunderflow.services.implementation.QuestionServiceImpl;
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
@ContextConfiguration(classes = QuestionController.class)
@WebMvcTest(QuestionController.class)
@Import({TestSecurityConfig.class, ModelAdvice.class, UserAdvice.class})
public class QuestionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionServiceImpl questionService;

    private UserResponseDto userResponseDto;

    private UserEntity user;

    private Question question;

    private QuestionResponseDto questionResponseDto;

    private final String username = "Mihnea";
    private final String password = "12345";

    @BeforeEach
    public void init() {
        String authority = "USER";
        var role = new Role(1L, authority);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        var topic = Topic.builder().topic(TopicEnum.SCIENCE).build();
        Set<Topic> topics = new HashSet<>();
        topics.add(topic);

        Set<TopicEnum> topicEnums = new HashSet<>();
        topicEnums.add(TopicEnum.SCIENCE);

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

        userResponseDto = new UserResponseDto(user.getUsername(), user.getPoints(), user.getBadge(), null);

        questionResponseDto = QuestionResponseDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .body(question.getBody())
                .createTimestamp(question.getCreateTimestamp())
                .updateTimestamp(question.getUpdateTimestamp())
                .user(userResponseDto)
                .topics(topicEnums)
                .build();
    }

    @DisplayName("JUnit test for get method")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenId_whenGet_thenReturnJson() throws Exception {
        var id = question.getId();

        given(questionService.get(id)).willReturn(questionResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/question/get/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(question.getId()))
                .andExpect(jsonPath("$.body").value(question.getBody()))
                .andExpect(jsonPath("$.title").value(question.getTitle()))
                .andExpect(jsonPath("$.topics").value(questionResponseDto.getTopics().toArray()[0].toString()))
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

        mockMvc.perform(MockMvcRequestBuilders.get("/question/get/0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(errorMessage));

    }
}
