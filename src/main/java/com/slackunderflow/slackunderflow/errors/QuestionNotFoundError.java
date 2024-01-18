package com.slackunderflow.slackunderflow.errors;

import com.slackunderflow.slackunderflow.enums.TopicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class QuestionNotFoundError extends ModelNotFoundError {
    public QuestionNotFoundError(String message, String body) {
        super(message, body);
    }
}
