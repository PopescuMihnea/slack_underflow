package com.slackunderflow.slackunderflow.service;

import com.slackunderflow.slackunderflow.advices.ModelAdvice;
import com.slackunderflow.slackunderflow.advices.UserAdvice;
import com.slackunderflow.slackunderflow.dtos.UserDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;
import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import com.slackunderflow.slackunderflow.errors.UserNotFoundError;
import com.slackunderflow.slackunderflow.mappers.UserMapper;
import com.slackunderflow.slackunderflow.models.Role;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.RoleRepository;
import com.slackunderflow.slackunderflow.repositories.UserEntityRepository;
import com.slackunderflow.slackunderflow.security.TokenService;
import com.slackunderflow.slackunderflow.services.QuestionService;
import com.slackunderflow.slackunderflow.services.implementation.UserEntityServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Import({ModelAdvice.class, UserAdvice.class})
public class UserServiceTests {
    @Mock
    private UserMapper userMapper;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserEntityServiceImpl userEntityService;

    private final String authority = "USER";
    private Role role;
    private UserEntity user;
    private UserDto userDto;
    private UserResponseDto userResponseDto;
    private final String username = "Mihnea";
    private final String token = "JWT here";

    private final Authentication auth = Mockito.mock(Authentication.class);

    @BeforeEach
    public void setup() {

    }

    private Set<Role> setRoles() {
        role = new Role(1L, authority);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        return roles;
    }

    private void customSetup(Set<Role> roles) {
        String password = "12345";
        userDto = new UserDto(username, password);

        user = UserEntity
                .builder()
                .id(1L)
                .badge(BadgeEnum.BEGINNER)
                .password("Hashed password")
                .points(0)
                .username(username)
                .authorities(roles).build();

        userResponseDto = new UserResponseDto(user.getUsername(), user.getPoints(), user.getBadge(), token);
    }

    private void stubAuthAndCreateToken() {
        given(authenticationManager.authenticate(Mockito.any()))
                .willReturn(auth);

        given(tokenService.generateJwt(auth)).willReturn(token);
        given(userEntityRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
    }

    @DisplayName("JUnit test for register method")
    @Test
    public void givenUserEntityObject_whenRegisterUserEntity_thenReturnUserResponseDto() {
        var roles = setRoles();

        customSetup(roles);

        given(roleRepository.findByAuthority(authority))
                .willReturn(Optional.of(role));

        given(userMapper.fromDtoToEntity(userDto, roles))
                .willReturn(user);

        stubAuthAndCreateToken();
        given(userMapper.fromEntityToResponseDto(user, token)).willReturn(userResponseDto);

        var createdUser = userEntityService.register(userDto);
        System.out.println("Test1 UserService result: " + createdUser.toString());

        assertThat(createdUser).isEqualTo(userResponseDto);
    }

    @DisplayName("JUnit test for register method that throws error")
    @Test
    public void givenUserEntityObject_whenRegisterUserEntity_thenReturnError() {
        String expectedMessage = "Role not found";
        given(roleRepository.findByAuthority(authority)).willReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(RuntimeException.class, () -> userEntityService.register(userDto));
        System.out.println("Test1 error UserService result: " + ex.toString());

        assertThat(ex.getMessage()).isEqualTo(expectedMessage);
    }

    @DisplayName("JUnit test for login method")
    @Test
    public void givenUserEntityObject_whenLoginUserEntity_thenReturnUserResponseDto() {
        role = new Role(1L, authority);
        var roles = setRoles();

        customSetup(roles);

        stubAuthAndCreateToken();
        given(userMapper.fromEntityToResponseDto(user, token)).willReturn(userResponseDto);

        var loggedUser = userEntityService.login(userDto);
        System.out.println("Test2 UserService result: " + loggedUser.toString());

        assertThat(loggedUser).isEqualTo(userResponseDto);
    }

    @DisplayName("JUnit test for login method that throws error")
    @Test
    public void givenUserEntityObject_whenLoginUserEntity_thenReturnError() {
        String expectedMessage = "User not found";

        role = new Role(1L, authority);
        var roles = setRoles();

        customSetup(roles);

        stubAuthAndCreateToken();
        given(userEntityRepository.findByUsername(username)).willReturn(Optional.empty());

        UserNotFoundError ex = Assertions.assertThrows(UserNotFoundError.class, () -> userEntityService.login(userDto));
        System.out.println("Test2 error UserService result: " + ex.toString());

        assertThat(ex.getMessage()).isEqualTo(expectedMessage);
        assertThat(ex.getUsername()).isEqualTo(username);
    }

    @DisplayName("JUnit test for modify method ")
    @Test
    public void givenUserEntityObject_whenModifyUserEntity_thenReturnUserResponseDto() {
        role = new Role(1L, authority);
        var roles = setRoles();

        customSetup(roles);
        userDto.setPassword(userDto.getPassword() + "modified");

        given(userEntityRepository.findByUsername(userDto.getUsername())).willReturn(Optional.of(user));
        given(passwordEncoder.encode(userDto.getPassword())).willReturn("Encoded password xD");
        given(userMapper.fromEntityToResponseDto(user, null)).willReturn(userResponseDto);

        var modifiedUser = userEntityService.modify(userDto);
        System.out.println("Test3 UserService result: " + modifiedUser.toString());

        assertThat(modifiedUser).isEqualTo(userResponseDto);
    }

    @DisplayName("JUnit test for modify method that throws error")
    @Test
    public void givenUserEntityObject_whenModifyUserEntity_thenReturnError() {
        String expectedMessage = "User not found";

        role = new Role(1L, authority);
        var roles = setRoles();

        customSetup(roles);

        given(userEntityRepository.findByUsername(username)).willReturn(Optional.empty());
        ;

        UserNotFoundError ex = Assertions.assertThrows(UserNotFoundError.class, () -> userEntityService.modify(userDto));
        System.out.println("Test3 error UserService result: " + ex.toString());

        assertThat(ex.getMessage()).isEqualTo(expectedMessage);
        assertThat(ex.getUsername()).isEqualTo(username);
    }

    @DisplayName("JUnit test for get method")
    @Test
    public void givenUserEntityObject_whenGetUserEntity_thenReturnUserResponseDto() {
        role = new Role(1L, authority);
        var roles = setRoles();

        customSetup(roles);

        given(userEntityRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userMapper.fromEntityToResponseDto(user, null)).willReturn(userResponseDto);

        var user = userEntityService.get(this.user.getId());
        System.out.println("Test4 UserService result: " + user.toString());

        assertThat(user).isEqualTo(userResponseDto);
    }

    @DisplayName("JUnit test for get method that throws error")
    @Test
    public void givenUserEntityObject_whenGetUserEntity_thenReturnError() {
        String expectedMessage = "User not found with id: ";

        role = new Role(1L, authority);
        var roles = setRoles();

        customSetup(roles);

        given(userEntityRepository.findById(user.getId())).willReturn(Optional.empty());
        ;

        UserNotFoundError ex = Assertions.assertThrows(UserNotFoundError.class, () -> userEntityService.get(user.getId()));
        System.out.println("Test4 error UserService result: " + ex.toString());

        assertThat(ex.getMessage()).isEqualTo(expectedMessage);
        assertThat(ex.getUsername()).isEqualTo(user.getId().toString());
    }

    @DisplayName("JUnit test for delete method")
    @Test
    public void givenUsername_whenDeleteUserEntity_thenReturnTrue() {
        role = new Role(1L, authority);
        var roles = setRoles();

        customSetup(roles);

        given(userEntityRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(questionService.deleteByUser(user)).willReturn(true);
        given(userEntityRepository.deleteByUsername(username)).willReturn(1L);

        var result = userEntityService.delete(username);
        System.out.println("Test5 UserService result: " + result);

        assertThat(result).isEqualTo(true);
    }

    @DisplayName("JUnit test for delete method that returns false because question deletion failed")
    @Test
    public void givenUsername_whenDeleteUserEntity_thenReturnFalse() {
        role = new Role(1L, authority);
        var roles = setRoles();

        customSetup(roles);

        given(userEntityRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(questionService.deleteByUser(user)).willReturn(false);

        var result = userEntityService.delete(username);
        System.out.println("Test5 error 1 UserService result: " + result);

        assertThat(result).isEqualTo(false);
    }

    @DisplayName("JUnit test for delete method that returns false because user deletion failed")
    @Test
    public void givenUsername_whenDeleteUserEntity_thenReturnFalse2() {
        role = new Role(1L, authority);
        var roles = setRoles();

        customSetup(roles);

        given(userEntityRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(questionService.deleteByUser(user)).willReturn(true);
        given(userEntityRepository.deleteByUsername(username)).willReturn(0L);

        var result = userEntityService.delete(username);
        System.out.println("Test5 error 1 UserService result: " + result);

        assertThat(result).isEqualTo(false);
    }

}
