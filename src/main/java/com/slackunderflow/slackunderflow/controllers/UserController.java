package com.slackunderflow.slackunderflow.controllers;

import com.slackunderflow.slackunderflow.dtos.UserDto;
import com.slackunderflow.slackunderflow.dtos.responses.UserResponseDto;
import com.slackunderflow.slackunderflow.errors.UserNotFoundError;
import com.slackunderflow.slackunderflow.services.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.models.examples.Example;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserEntityService userEntityService;


    @Operation(summary = "Gets information about a user with the provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The user has been found and his information returned in the body",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable
                                                       @Parameter(description = "Id of the user to get information for",
                                                               example = "1")
                                                       @Min(1)
                                                       long id) {
        return new ResponseEntity<>(userEntityService.get(id), HttpStatus.OK);
    }

    @Operation(summary = "Registers the user, adding him to the database, and sends a jwt token to be used in authenticating the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "The user has been succesfully created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Validated(UserDto.RegisteringUser.class)
                                                    @io.swagger.v3.oas.annotations.parameters.
                                                            RequestBody(description = "The data of the user you want to register")
                                                    @RequestBody
                                                    UserDto userDto) {
        UserResponseDto userResponseDto = userEntityService.register(userDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Logs in the user, sending a jwt token to be used in authenticating the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The user has successfully logged in",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
    })
    @PutMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid
                                                 @io.swagger.v3.oas.annotations.parameters.
                                                         RequestBody(description = "The data of the user you want to log in as")
                                                 @RequestBody
                                                 UserDto userDto) {
        return new ResponseEntity<>(
                userEntityService.login(userDto),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Modifies the logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Modified the user with the data provided",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
    })
    @PutMapping("/modify")
    public ResponseEntity<UserResponseDto> modify(Authentication authentication,
                                                  @Valid
                                                  @io.swagger.v3.oas.annotations.parameters.
                                                          RequestBody(description = "The data of the user that is to be modified")
                                                  @RequestBody
                                                  UserDto userDto) {
        String name = authentication.getName();

        if (Objects.equals(name, userDto.getUsername())) {
            return new ResponseEntity<>(
                    userEntityService.modify(userDto),
                    HttpStatus.OK
            );
        } else {
            throw new UserNotFoundError("User is not found", userDto.getUsername());
        }
    }

    @Operation(summary = "Deletes the user currently logged in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The user has been deleted",
                    content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = "User has been deleted")})}),
            @ApiResponse(responseCode = "400",
                    description = "User deletion failed due to faulty data or database error",
                    content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = "User deletion failed")})}),
    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(Authentication authentication) {
        String name = authentication.getName();
        boolean result = userEntityService.delete(name);

        return new ResponseEntity<>(
                result ? "User has been deleted" : "User deletion failed",
                result ? HttpStatus.OK : HttpStatus.BAD_REQUEST
        );
    }

}
