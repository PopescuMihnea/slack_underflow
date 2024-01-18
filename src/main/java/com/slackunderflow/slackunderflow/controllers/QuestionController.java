package com.slackunderflow.slackunderflow.controllers;


import com.slackunderflow.slackunderflow.dtos.requests.QuestionRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.QuestionResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.SuggestionResponseDto;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
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

import java.util.List;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@Validated
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "Gets a list of all questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried questions",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = QuestionResponseDto.class)))}),
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestions() {
        return new ResponseEntity<>(questionService.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Gets a list of all the questions that include requested topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried suggestions by given topics",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = QuestionResponseDto.class)))}),
    })
    @GetMapping("/getAllByTopics")
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestionsByTopics(@Valid
                                                                             @io.swagger.v3.oas.annotations.parameters.
                                                                                     RequestBody(description = "A list of the topics that the returend questions should include")
                                                                             @RequestBody
                                                                             List<TopicEnum> topics) {
        return new ResponseEntity<>(questionService.getAllByTopics(topics), HttpStatus.OK);
    }

    @Operation(summary = "Gets a list of all the questions that include requested title string")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried suggestions by given title",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = QuestionResponseDto.class)))}),
    })
    @GetMapping("/getAllByTitle")
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestionsByTitle(@Valid
                                                                            @io.swagger.v3.oas.annotations.parameters.
                                                                                    RequestBody(description = "String that should be found in the questions' title")
                                                                            @RequestBody
                                                                            String title) {
        return new ResponseEntity<>(questionService.getAllByTitle(title), HttpStatus.OK);
    }

    @Operation(summary = "Gets the question with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully found the question with the given id",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = QuestionResponseDto.class))}),
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestion(@PathVariable
                                                           @Parameter(description = "The id of the question you want to get information for")
                                                           @Min(1)
                                                           long id) {
        return new ResponseEntity<>(questionService.get(id), HttpStatus.OK);
    }

    @Operation(summary = "Gets a list of all the questions that have the given user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried suggestions by given user id",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = QuestionResponseDto.class)))}),
    })
    @GetMapping("/get/user/{id}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsByUser(@PathVariable
                                                                        @Parameter(description = "The id of the user that the question should have")
                                                                        @Min(1)
                                                                        long id) {
        return new ResponseEntity<>(questionService.getAllByUser(id), HttpStatus.OK);
    }

    @Operation(summary = "Gets a list of all the questions that have the given user username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried suggestions by given user username",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = QuestionResponseDto.class)))}),
    })
    @GetMapping("/get/user/{username}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsByUser(@PathVariable
                                                                        @Parameter(description = "The id of the user that the question should have")
                                                                        @NotBlank
                                                                        String username) {
        return new ResponseEntity<>(questionService.getAllByUser(username), HttpStatus.OK);
    }

    @Operation(summary = "Creates a new question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully created question with the given data",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = QuestionResponseDto.class))}),
    })
    @PostMapping("/create")
    public ResponseEntity<QuestionResponseDto> createQuestion(Authentication authentication,
                                                              @Valid
                                                              @io.swagger.v3.oas.annotations.parameters.
                                                                      RequestBody(description = "The data of the question that is to be created")
                                                              @RequestBody
                                                              QuestionRequestDto questionRequestDto) {
        String name = authentication.getName();

        return new ResponseEntity<>(questionService.create(questionRequestDto, name), HttpStatus.CREATED);

    }

    @Operation(summary = "Modifies a question with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully modified question with the given data and id",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = QuestionResponseDto.class))}),
    })
    @PutMapping("/modify/{id}")
    public ResponseEntity<QuestionResponseDto> modifyQuestion(Authentication authentication,
                                                              @Valid
                                                              @io.swagger.v3.oas.annotations.parameters.
                                                                      RequestBody(description = "The data to modify the question with")
                                                              @RequestBody
                                                              QuestionRequestDto questionRequestDto,
                                                              @PathVariable
                                                              @Parameter(description = "The id of the question that is to be modified")
                                                              @Min(1)
                                                              long id) {
        String name = authentication.getName();

        return new ResponseEntity<>(questionService.modify(id, questionRequestDto, name), HttpStatus.OK);
    }

    @Operation(summary = "Deletes the question, belonging to the authenticated user, with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The question has been deleted",
                    content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = "Question has been deleted")})}),
            @ApiResponse(responseCode = "400",
                    description = "Question deletion failed due to wrong id or database error",
                    content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = "Question deletion failed")})}),
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuestion(Authentication authentication,
                                                 @PathVariable
                                                 @Parameter(description = "The id of the question, belonging to the user, to delete")
                                                 @Min(1)
                                                 long id) {
        String name = authentication.getName();

        var result = questionService.delete(id, name);
        return new ResponseEntity<>(
                result ? "Question has been deleted" : "Question deletion failed",
                result ? HttpStatus.OK : HttpStatus.BAD_REQUEST
        );
    }
}
