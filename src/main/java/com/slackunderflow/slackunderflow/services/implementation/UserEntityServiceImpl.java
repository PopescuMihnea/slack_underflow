package com.slackunderflow.slackunderflow.services.implementation;

import com.slackunderflow.slackunderflow.dtos.UserDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;
import com.slackunderflow.slackunderflow.errors.UserNotFoundError;
import com.slackunderflow.slackunderflow.mappers.UserMapper;
import com.slackunderflow.slackunderflow.models.Role;
import com.slackunderflow.slackunderflow.repositories.RoleRepository;
import com.slackunderflow.slackunderflow.repositories.UserEntityRepository;
import com.slackunderflow.slackunderflow.security.TokenService;
import com.slackunderflow.slackunderflow.services.QuestionService;
import com.slackunderflow.slackunderflow.services.UserEntityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserEntityServiceImpl implements UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final QuestionService questionService;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserDto userDto) {

        Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        userEntityRepository.save(userMapper.fromDtoToEntity(userDto, roles));

        return authAndCreateToken(userDto);
    }

    public UserResponseDto login(UserDto userDto) {

        return authAndCreateToken(userDto);

    }

    @Override
    public UserResponseDto modify(UserDto userDto) {
        String username = userDto.getUsername();

        var user = userEntityRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> new UserNotFoundError("User not found", username));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntityRepository.save(user);

        return userMapper.fromEntityToResponseDto(user, null);
    }

    @Override
    public UserResponseDto get(Long id) {
        var user = userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundError("User not found with id: ", id.toString()));

        return userMapper.fromEntityToResponseDto(user, null);
    }

    @Override
    public boolean delete(String username) {
        var user = userEntityRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundError("User is not found", username));

        if (!questionService.deleteByUser(user)) {
            return false;
        }

        return userEntityRepository.deleteByUsername(username) == 1;
    }


    private UserResponseDto authAndCreateToken(UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String token = tokenService.generateJwt(auth);

        var savedUser = userEntityRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundError("User not found", username));
        return userMapper.fromEntityToResponseDto(savedUser, token);
    }

}
