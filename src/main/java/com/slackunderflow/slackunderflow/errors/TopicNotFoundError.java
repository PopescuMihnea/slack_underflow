package com.slackunderflow.slackunderflow.errors;

import com.slackunderflow.slackunderflow.enums.TopicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class TopicNotFoundError extends ModelNotFoundError {
    public TopicNotFoundError(String message, TopicEnum topicEnum) {
        super(message, topicEnum.toString());
    }

    public TopicNotFoundError(String message, Long id) {
        super(message, id.toString());
    }

}
