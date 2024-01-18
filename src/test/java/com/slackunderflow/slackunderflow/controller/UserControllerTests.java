package com.slackunderflow.slackunderflow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.slackunderflow.slackunderflow.advices.ModelAdvice;
import com.slackunderflow.slackunderflow.advices.UserAdvice;
import com.slackunderflow.slackunderflow.configuration.TestSecurityConfig;
import com.slackunderflow.slackunderflow.controllers.UserController;
import com.slackunderflow.slackunderflow.dtos.UserDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;
import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import com.slackunderflow.slackunderflow.errors.UserNotFoundError;
import com.slackunderflow.slackunderflow.models.Role;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.services.implementation.UserEntityServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserController.class)
@WebMvcTest(UserController.class)
@Import({TestSecurityConfig.class, ModelAdvice.class, UserAdvice.class})
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserEntityServiceImpl userEntityService;

    private UserEntity user;
    private UserDto userDto;
    private UserResponseDto userResponseDto;
    private final String username = "Mihnea";
    private final String password = "12345";

    private static ObjectWriter writer;

    @BeforeAll
    public static void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        writer = mapper.writer().withDefaultPrettyPrinter();
    }

    @BeforeEach
    public void init() {
        String authority = "USER";
        Role role = new Role(1L, authority);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        userDto = new UserDto(username, password);

        user = UserEntity
                .builder()
                .id(1L)
                .badge(BadgeEnum.BEGINNER)
                .password("Hashed password")
                .points(0)
                .username(username)
                .authorities(roles).build();

        var token = "JWT here";
        userResponseDto = new UserResponseDto(user.getUsername(), user.getPoints(), user.getBadge(), token);
    }

    @DisplayName("JUnit test for get method")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenId_whenGet_thenReturnJson() throws Exception {
        given(userEntityService.get(user.getId())).willReturn(userResponseDto);


        mockMvc.perform(MockMvcRequestBuilders.get("/user/get/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.points").value(user.getPoints()))
                .andExpect(jsonPath("$.badge").value(user.getBadge().toString()))
                .andExpect(jsonPath("$.jwt").value(userResponseDto.getJwt()));
    }

    @DisplayName("JUnit test for get method that throws error")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenUserNotFoundError_whenGet_thenReturnJson() throws Exception {
        var errorMessage = "User not found with id: ";
        Long searchId = 2L;

        given(userEntityService.get(searchId))
                .willAnswer(i -> {
                    throw new UserNotFoundError("User not found with id: ", searchId.toString());
                });


        mockMvc.perform(MockMvcRequestBuilders.get("/user/get/2"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.username").value(searchId.toString()))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @DisplayName("JUnit test for get method that throws error from validation")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenId_whenGet_thenReturnError() throws Exception {
        var errorMessage = "must be greater than or equal to 1";

        mockMvc.perform(MockMvcRequestBuilders.get("/user/get/0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(errorMessage));
    }

    @DisplayName("JUnit test for register method")
    @Test
    public void givenUser_whenRegister_thenReturnJson() throws Exception {

        given(userEntityService.register(userDto)).willReturn(userResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(userDto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.points").value(user.getPoints()))
                .andExpect(jsonPath("$.badge").value(user.getBadge().toString()))
                .andExpect(jsonPath("$.jwt").value(userResponseDto.getJwt()));
    }

    @DisplayName("JUnit test for register method that throws error")
    @Test
    public void givenSQLError_whenRegister_thenReturnJson() throws Exception {
        var errorMessage = "Duplicate entry 'Mihnea' for key 'utilizator.UK_d2wc54g88nka04vi4nhe9hdn9'";

        given(userEntityService.register(userDto))
                .willAnswer(i -> {
                    throw new SQLIntegrityConstraintViolationException(errorMessage);
                });

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @DisplayName("JUnit test for register method that throws validation error")
    @Test
    public void givenUserInvalid_whenRegister_thenReturnJson() throws Exception {
        var errorMessasge = "You need to enter a username";
        userDto.setUsername("");

        given(userEntityService.register(userDto)).willReturn(userResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(userDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value(errorMessasge));
    }

    @DisplayName("JUnit test for login method")
    @Test
    public void givenUser_whenLogin_thenReturnJson() throws Exception {

        given(userEntityService.login(userDto)).willReturn(userResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(userDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.points").value(user.getPoints()))
                .andExpect(jsonPath("$.badge").value(user.getBadge().toString()))
                .andExpect(jsonPath("$.jwt").value(userResponseDto.getJwt()));
    }

    @DisplayName("JUnit test for login method that throws validation error")
    @Test
    public void givenUserInvalid_whenLogin_thenReturnJson() throws Exception {
        var errorMessasge = "You need to enter a password";
        userDto.setPassword("");

        given(userEntityService.login(userDto)).willReturn(userResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(userDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value(errorMessasge));
    }

    @DisplayName("JUnit test for modify method")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenUser_whenModify_thenReturnJson() throws Error, Exception {
        var userDtoModify = new UserDto(username, password + "abc");
        user.setPassword(userDtoModify.getPassword());

        var userResponseDto = new UserResponseDto(user.getUsername(), user.getPoints(), user.getBadge(), null);

        given(userEntityService.modify(userDtoModify)).willReturn(userResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(userDtoModify))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.points").value(user.getPoints()))
                .andExpect(jsonPath("$.badge").value(user.getBadge().toString()));
    }

    @DisplayName("JUnit test for modify method that throws exception")
    @Test
    @WithMockUser(username = "Sussy", password = password)
    public void givenUserNotFoundError_whenModify_thenReturnJson() throws Error, Exception {
        var userDtoModify = new UserDto(username, password + "abc");
        user.setPassword(userDtoModify.getPassword());

        var userResponseDto = new UserResponseDto(user.getUsername(), user.getPoints(), user.getBadge(), null);

        given(userEntityService.modify(userDtoModify)).willReturn(userResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(userDtoModify))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.message").value("User is not found"));
    }

    @DisplayName("JUnit test for delete method")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenUser_whenDelete_thenReturnString() throws Error, Exception {
        String expectedContent = "User has been deleted";

        given(userEntityService.delete(username)).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(expectedContent));
    }

    @DisplayName("JUnit test for delete method failure")
    @Test
    @WithMockUser(username = username, password = password)
    public void givenUser_whenDelete_thenReturnStringFailed() throws Error, Exception {
        String expectedContent = "User deletion failed";

        given(userEntityService.delete(username)).willReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(expectedContent));
    }
}
