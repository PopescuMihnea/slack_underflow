package com.slackunderflow.slackunderflow.controller;

import com.slackunderflow.slackunderflow.advices.ModelAdvice;
import com.slackunderflow.slackunderflow.advices.UserAdvice;
import com.slackunderflow.slackunderflow.configuration.TestSecurityConfig;
import com.slackunderflow.slackunderflow.controllers.AnswerController;
import com.slackunderflow.slackunderflow.controllers.TopicController;
import com.slackunderflow.slackunderflow.dtos.TopicDto;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.models.Topic;
import com.slackunderflow.slackunderflow.services.implementation.AnswerServiceImpl;
import com.slackunderflow.slackunderflow.services.implementation.TopicServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TopicController.class)
@WebMvcTest(TopicController.class)
@Import({TestSecurityConfig.class, ModelAdvice.class, UserAdvice.class})
public class TopicControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicServiceImpl topicService;

    private final String username = "Mihnea";
    private final String password = "12345";

    @DisplayName("JUnit test for getAll method")
    @Test
    @WithMockUser(username = username, password = password)
    public void whenGetAll_thenReturnJson() throws Exception {
        var topicDto1 = TopicDto.builder().topic(TopicEnum.SCIENCE).build();
        var topicDto2 = TopicDto.builder().topic(TopicEnum.PHILOSOPHY).build();
        List<TopicDto> topicDtos = new ArrayList<>();
        topicDtos.add(topicDto1);
        topicDtos.add(topicDto2);

        given(topicService.getAll()).willReturn(topicDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/topic/getAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value(topicDtos.get(0)))
                .andExpect(jsonPath("$.[1]").value(topicDtos.get(1)));
    }
}
