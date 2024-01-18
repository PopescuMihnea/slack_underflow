package com.slackunderflow.slackunderflow.errors;

import lombok.Getter;

@Getter
public class ModelNotFoundError extends RuntimeException {
    private final String body;

    public ModelNotFoundError(String message, String body) {
        super(message);
        this.body = body;
    }
}
