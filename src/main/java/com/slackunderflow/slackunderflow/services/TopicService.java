package com.slackunderflow.slackunderflow.services;

import com.slackunderflow.slackunderflow.dtos.TopicDto;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.models.Question;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TopicService {
    TopicDto get(TopicEnum topicEnum);

    List<TopicDto> getAll();
}
