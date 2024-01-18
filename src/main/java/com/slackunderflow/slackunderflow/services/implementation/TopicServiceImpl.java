package com.slackunderflow.slackunderflow.services.implementation;

import com.slackunderflow.slackunderflow.dtos.TopicDto;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.errors.TopicNotFoundError;
import com.slackunderflow.slackunderflow.errors.UserNotFoundError;
import com.slackunderflow.slackunderflow.mappers.TopicMapper;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.Topic;
import com.slackunderflow.slackunderflow.repositories.QuestionRepository;
import com.slackunderflow.slackunderflow.repositories.TopicRepository;
import com.slackunderflow.slackunderflow.repositories.UserEntityRepository;
import com.slackunderflow.slackunderflow.services.TopicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TopicServiceImpl implements TopicService {

    private final TopicMapper topicMapper;
    private final TopicRepository topicRepository;

    @Override
    public TopicDto get(TopicEnum topicEnum) {
        Topic topic = topicRepository.findByTopic(topicEnum).orElseThrow(() -> new TopicNotFoundError("Topic not found ", topicEnum));

        return topicMapper.fromEntityToDto(topic);
    }

    @Override
    public List<TopicDto> getAll() {

        return topicRepository
                .findAll()
                .stream()
                .map(topicMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }
}
