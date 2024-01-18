package com.slackunderflow.slackunderflow.controllers;

import com.slackunderflow.slackunderflow.dtos.TopicDto;
import com.slackunderflow.slackunderflow.dtos.requests.SuggestionRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.SuggestionResponseDto;
import com.slackunderflow.slackunderflow.services.SuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suggestion")
@RequiredArgsConstructor
@Validated
public class SuggestionController {
    private final SuggestionService suggestionService;

    @Operation(summary = "Gets a list of all suggestions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried suggestions",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SuggestionResponseDto.class)))}),
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<SuggestionResponseDto>> getAllSuggestions() {
        return new ResponseEntity<>(suggestionService.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Gets a suggestion with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully found suggestion info",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuggestionResponseDto.class))}),
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<SuggestionResponseDto> getSuggestion(@PathVariable
                                                               @Parameter(description = "The id of the suggestion to get info for")
                                                               @Min(1)
                                                               long id) {
        return new ResponseEntity<>(suggestionService.get(id), HttpStatus.OK);
    }

    @Operation(summary = "Gets all suggestions created by the user with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully found suggestions of the user",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SuggestionResponseDto.class)))}),
    })
    @GetMapping("/get/user/{id}")
    public ResponseEntity<List<SuggestionResponseDto>> getSuggestionsByUser(@PathVariable
                                                                            @Parameter(description = "The id of the user which all of the suggestions will belong to")
                                                                            @Min(1)
                                                                            long id) {
        return new ResponseEntity<>(suggestionService.getAllByUser(id), HttpStatus.OK);
    }

    @Operation(summary = "Gets all suggestions that belong to the answer with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully found suggestions of the answer",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SuggestionResponseDto.class)))}),
    })
    @GetMapping("/get/question/{answerId}")
    public ResponseEntity<List<SuggestionResponseDto>> getSuggestionsByAnswer(@PathVariable
                                                                              @Parameter(description = "The id of the answer that the suggestions should belong to")
                                                                              @Min(1)
                                                                              long answerId) {
        return new ResponseEntity<>(suggestionService.getAllByAnswer(answerId), HttpStatus.OK);
    }

    @Operation(summary = "Gets all suggestions that belong to the user with the given username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully found suggestions of the user",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SuggestionResponseDto.class)))}),
    })
    @GetMapping("/get/user/{username}")
    public ResponseEntity<List<SuggestionResponseDto>> getSuggestionsByUser(@PathVariable
                                                                            @Parameter(description = "The username of the user which all of the suggestions will belong to")
                                                                            @NotBlank
                                                                            String username) {
        return new ResponseEntity<>(suggestionService.getAllByUser(username), HttpStatus.OK);
    }

    @Operation(summary = "Creates a new suggestion for an answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully created suggestion",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuggestionResponseDto.class))}),
    })
    @PostMapping("/create")
    public ResponseEntity<SuggestionResponseDto> createSuggestion(Authentication authentication,
                                                                  @Valid
                                                                  @io.swagger.v3.oas.annotations.parameters.
                                                                          RequestBody(description = "The data for the suggestion that is to be created")
                                                                  @RequestBody
                                                                  SuggestionRequestDto suggestionRequestDto) {
        String name = authentication.getName();

        return new ResponseEntity<>(suggestionService.create(suggestionRequestDto, name), HttpStatus.CREATED);

    }

    @Operation(summary = "Modifies a suggestion with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully modified suggestion",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuggestionResponseDto.class))}),
    })
    @PutMapping("/modify/{id}")
    public ResponseEntity<SuggestionResponseDto> modifySuggestion(Authentication authentication,
                                                                  @Valid
                                                                  @io.swagger.v3.oas.annotations.parameters.
                                                                          RequestBody(description = "The data of the answer to modify")
                                                                  @RequestBody
                                                                  SuggestionRequestDto suggestionRequestDto,
                                                                  @PathVariable
                                                                  @Parameter(description = "The id of the suggestion that is to be modified")
                                                                  @Min(1)
                                                                  long id) {
        String name = authentication.getName();

        return new ResponseEntity<>(suggestionService.modify(id, suggestionRequestDto, name), HttpStatus.OK);
    }

    @Operation(summary = "Deletes the suggestion, belonging to the authenticated user, with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The suggestion has been deleted",
                    content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = "Suggestion has been deleted")})}),
            @ApiResponse(responseCode = "400",
                    description = "Suggestion deletion failed due to wrong id or database error",
                    content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = "Suggestion deletion failed")})}),
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSuggestion(Authentication authentication,
                                                   @PathVariable
                                                   @Parameter(description = "The id of the suggestion, owned by the logged in user, you want to delete")
                                                   @Min(1)
                                                   long id) {
        String name = authentication.getName();

        var result = suggestionService.delete(id, name);
        return new ResponseEntity<>(
                result ? "Suggestion has been deleted" : "Suggestion deletion failed",
                result ? HttpStatus.OK : HttpStatus.BAD_REQUEST
        );
    }
}
