package com.slackunderflow.slackunderflow.services;

import com.slackunderflow.slackunderflow.dtos.UserDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;

public interface UserEntityService {
    UserResponseDto register(UserDto userDto);

    UserResponseDto login(UserDto userLoginDto);

    UserResponseDto modify(UserDto userDto);

    UserResponseDto get(Long id);

    boolean delete(String username);
}
