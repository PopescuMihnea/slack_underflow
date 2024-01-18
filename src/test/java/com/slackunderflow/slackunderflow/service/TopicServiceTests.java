package com.slackunderflow.slackunderflow.service;

import com.slackunderflow.slackunderflow.advices.ModelAdvice;
import com.slackunderflow.slackunderflow.advices.UserAdvice;
import com.slackunderflow.slackunderflow.dtos.TopicDto;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.mappers.TopicMapper;
import com.slackunderflow.slackunderflow.models.Topic;
import com.slackunderflow.slackunderflow.repositories.TopicRepository;
import com.slackunderflow.slackunderflow.services.implementation.TopicServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTests {
    @Mock
    private TopicMapper topicMapper;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicServiceImpl topicService;

    @DisplayName("JUnit test for getAll method")
    @Test
    public void whenGetAllTopic_thenReturnJson() {
        var topic1 = Topic.builder().topic(TopicEnum.PHILOSOPHY).build();
        var topic2 = Topic.builder().topic(TopicEnum.SCIENCE).build();

        List<Topic> topics = new ArrayList<>();
        topics.add(topic1);
        topics.add(topic2);

        var topicDto1 = TopicDto.builder().topic(TopicEnum.PHILOSOPHY).build();
        var topicDto2 = TopicDto.builder().topic(TopicEnum.SCIENCE).build();

        List<TopicDto> topicDtos = new ArrayList<>();
        topicDtos.add(topicDto1);
        topicDtos.add(topicDto2);

        given(topicRepository.findAll()).willReturn(topics);
        given(topicMapper.fromEntityToDto(topic1)).willReturn(topicDto1);
        given(topicMapper.fromEntityToDto(topic2)).willReturn(topicDto2);

        var responseTopics = topicService.getAll();
        System.out.println("Test1 Topics result: " + responseTopics.toString());

        assertThat(responseTopics).isEqualTo(topicDtos);
    }
}
