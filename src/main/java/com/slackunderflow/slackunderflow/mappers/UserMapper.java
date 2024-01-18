package com.slackunderflow.slackunderflow.mappers;


import com.slackunderflow.slackunderflow.dtos.UserDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;
import com.slackunderflow.slackunderflow.models.Role;
import com.slackunderflow.slackunderflow.models.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserResponseDto fromEntityToResponseDto(UserEntity userEntity, String jwt) {
        return UserResponseDto.builder()
                .username(userEntity.getUsername())
                .badge(userEntity.getBadge())
                .jwt(jwt)
                .points(userEntity.getPoints()).build();
    }

    public UserEntity fromDtoToEntity(UserDto userDto, Set<Role> roles) {
        return UserEntity.builder()
                .username(userDto.getUsername())
                .authorities(roles)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

    }
}
