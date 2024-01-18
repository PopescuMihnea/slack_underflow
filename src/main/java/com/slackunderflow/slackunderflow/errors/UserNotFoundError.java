package com.slackunderflow.slackunderflow.errors;

import lombok.Data;
import lombok.Getter;

@Getter
public class UserNotFoundError extends RuntimeException {
    private final String username;
    private String password = "";

    public UserNotFoundError(String message, String username, String password) {
        super(message);
        this.username = username;
        this.password = password;
    }

    public UserNotFoundError(String message, String username) {
        super(message);
        this.username = username;
    }


}
