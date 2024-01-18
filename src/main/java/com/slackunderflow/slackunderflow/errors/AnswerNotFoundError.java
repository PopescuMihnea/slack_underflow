package com.slackunderflow.slackunderflow.errors;

import lombok.Getter;

@Getter
public class AnswerNotFoundError extends ModelNotFoundError {

    public AnswerNotFoundError(String message, String body) {
        super(message, body);
    }
}
