package com.slackunderflow.slackunderflow.errors;

import lombok.Getter;

@Getter
public class SuggestionNotFoundError extends ModelNotFoundError {

    public SuggestionNotFoundError(String message, String body) {
        super(message, body);
    }
}

