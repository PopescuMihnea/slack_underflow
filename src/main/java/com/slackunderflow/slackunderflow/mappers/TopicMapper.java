package com.slackunderflow.slackunderflow.mappers;

import com.slackunderflow.slackunderflow.dtos.TopicDto;
import com.slackunderflow.slackunderflow.models.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicMapper {
    public TopicDto fromEntityToDto(Topic topic) {
        return TopicDto.builder()
                .topic(topic.getTopic()).build();
    }
}
