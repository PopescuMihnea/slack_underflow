package com.slackunderflow.slackunderflow.mappers;

import com.slackunderflow.slackunderflow.dtos.requests.QuestionRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.QuestionResponseDto;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.errors.TopicNotFoundError;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.Topic;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.QuestionRepository;
import com.slackunderflow.slackunderflow.repositories.TopicRepository;
import com.slackunderflow.slackunderflow.repositories.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuestionMapper extends BodyEntityMapper<Question, QuestionResponseDto, QuestionRequestDto> {

    private final TopicRepository topicRepository;

    public QuestionMapper(TopicRepository topicRepository, UserMapper userMapper) {
        super(userMapper);
        this.topicRepository = topicRepository;
    }

    public QuestionResponseDto fromEntityToResponse(Question question) {
        Set<Topic> topics = question.getTopics();
        Set<TopicEnum> topicEnums = topics.stream()
                .map(Topic::getTopic)
                .collect(Collectors.toSet());

        System.out.println("feafa:" + question.getCreateTimestamp());
        System.out.println(ZonedDateTime.now());

        return QuestionResponseDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .topics(topicEnums)
                .body(question.getBody())
                .createTimestamp(question.getCreateTimestamp())
                .updateTimestamp(question.getUpdateTimestamp())
                .user(userMapper.fromEntityToResponseDto(question.getUser(), null)).build();
    }

    public Question fromRequestToEntity(QuestionRequestDto questionRequestDto, UserEntity user) {
        Set<TopicEnum> topicEnums = questionRequestDto.getTopics();
        Set<Topic> topics = topicEnums
                .stream()
                .map(topic -> topicRepository.findByTopic(topic).orElseThrow(() -> new TopicNotFoundError("Topic not found", topic)))
                .collect(Collectors.toSet());

        return Question.builder()
                .title(questionRequestDto.getTitle())
                .body(questionRequestDto.getBody())
                .topics(topics)
                .user(user).build();
    }

}
