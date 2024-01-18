package com.slackunderflow.slackunderflow.controllers;

import com.slackunderflow.slackunderflow.dtos.requests.AnswerRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.AnswerResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.QuestionResponseDto;
import com.slackunderflow.slackunderflow.services.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
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
@RequestMapping("/answer")
@RequiredArgsConstructor
@Validated
public class AnswerController {
    private final AnswerService answerService;

    @Operation(summary = "Gets a list of all answers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried answers",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AnswerResponseDto.class)))}),
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<AnswerResponseDto>> getAllAnswers() {
        return new ResponseEntity<>(answerService.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Gets an answer with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully found answer with the id",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnswerResponseDto.class))}),
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<AnswerResponseDto> getAnswer(@PathVariable
                                                       @Parameter(description = "The id of the answer to get")
                                                       @Min(1)
                                                       long id) {
        return new ResponseEntity<>(answerService.get(id), HttpStatus.OK);
    }

    @Operation(summary = "Gets a list of all answers belonging to a user with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried answers of the user with the given id",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AnswerResponseDto.class)))}),
    })
    @GetMapping("/get/user/{id}")
    public ResponseEntity<List<AnswerResponseDto>> getAnswersByUser(@PathVariable
                                                                    @Parameter(description = "The id of the user which the answers belong to")
                                                                    @Min(1)
                                                                    long id) {
        return new ResponseEntity<>(answerService.getAllByUser(id), HttpStatus.OK);
    }

    @Operation(summary = "Gets a list of all answers belonging to the question with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried answers of the question with the given id",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AnswerResponseDto.class)))}),
    })
    @GetMapping("/get/question/{questionId}")
    public ResponseEntity<List<AnswerResponseDto>> getAnswersByQuestion(@PathVariable
                                                                        @Parameter(description = "The id of the question which the answers belong to")
                                                                        @Min(1)
                                                                        long questionId) {
        return new ResponseEntity<>(answerService.getAllByQuestion(questionId), HttpStatus.OK);
    }

    @Operation(summary = "Gets a list of all answers belonging to a user with the given username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully queried answers of the user with the given username",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AnswerResponseDto.class)))}),
    })
    @GetMapping("/get/user/{username}")
    public ResponseEntity<List<AnswerResponseDto>> getAnswersByUser(@PathVariable
                                                                    @Parameter(description = "The username of the user which the answers belong to")
                                                                    @NotBlank
                                                                    String username) {
        return new ResponseEntity<>(answerService.getAllByUser(username), HttpStatus.OK);
    }

    @Operation(summary = "Creates a new answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully created answer with the given data",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnswerResponseDto.class))}),
    })
    @PostMapping("/create")
    public ResponseEntity<AnswerResponseDto> createAnswer(Authentication authentication,
                                                          @Valid @io.swagger.v3.oas.annotations.parameters.
                                                                  RequestBody(description = "The data of the new answer")
                                                          @RequestBody
                                                          AnswerRequestDto answerRequestDto) {
        String name = authentication.getName();

        return new ResponseEntity<>(answerService.create(answerRequestDto, name), HttpStatus.CREATED);

    }

    @Operation(summary = "Resets the rank of the answers of a given question, by id, to 0")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully reset the ranks of all the answers of the question to 0",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AnswerResponseDto.class)))}),
    })
    @PatchMapping("/reset/{questionId}")
    public ResponseEntity<List<AnswerResponseDto>> resetRank(@PathVariable
                                                             @Parameter(description = "The id of the question for which to reset all answer ranks to 0")
                                                             @Min(1)
                                                             long questionId) {
        return new ResponseEntity<>(answerService.resetRanksByQuestion(questionId), HttpStatus.OK);
    }

    @Operation(summary = "Updates a answer with the given information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully modified answer with the given data",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnswerResponseDto.class))}),
    })
    @PatchMapping("/modify/{id}")
    public ResponseEntity<AnswerResponseDto> modifyAnswer(Authentication authentication,
                                                          @Valid
                                                          @io.swagger.v3.oas.annotations.parameters.
                                                                  RequestBody(description = "The data with which to modify the answer")
                                                          @RequestBody
                                                          AnswerRequestDto answerRequestDto,
                                                          @PathVariable
                                                          @Parameter(description = "The id of the answer to modify")
                                                          @Min(1)
                                                          long id) {
        String name = authentication.getName();

        return new ResponseEntity<>(answerService.modify(id, answerRequestDto, name), HttpStatus.OK);
    }

    @Operation(summary = "Updates a answer with the given rank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully modified answer with the given rank",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnswerResponseDto.class))}),
    })
    @PatchMapping("/modify/rank/{id}")
    public ResponseEntity<List<AnswerResponseDto>> updateRank(Authentication authentication,
                                                              @PathVariable
                                                              @Parameter(description = "The id of the answer of which to update rank")
                                                              @Min(1)
                                                              long id,
                                                              @RequestParam
                                                              @Parameter(description = "The new rank of the answer")
                                                              @Min(0)
                                                              @Max(3)
                                                              Integer rank) {
        String name = authentication.getName();

        return new ResponseEntity<>(answerService.updateRank(id, rank, name), HttpStatus.OK);
    }

    @Operation(summary = "Deletes the answer, belonging to the authenticated user, with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The answer has been deleted",
                    content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = "Answer has been deleted")})}),
            @ApiResponse(responseCode = "400",
                    description = "Answer deletion failed due to wrong id or database error",
                    content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class),
                            examples = {@ExampleObject(value = "Answer deletion failed")})}),
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAnswer(Authentication authentication,
                                               @PathVariable
                                               @Parameter(description = "The id of the question that is to be deleted")
                                               @Min(1)
                                               long id) {
        String name = authentication.getName();

        var result = answerService.delete(id, name);
        return new ResponseEntity<>(
                result ? "Answer has been deleted" : "Answer deletion failed",
                result ? HttpStatus.OK : HttpStatus.BAD_REQUEST
        );
    }
}
