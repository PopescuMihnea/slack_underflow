package com.slackunderflow.slackunderflow.dtos.responses;

import com.slackunderflow.slackunderflow.models.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class BodyResponse {
    @Schema(description = "The id of this model")
    private Long id;
    @Schema(description = "The body of this model, contains the text that is showed in the front end")
    private String body;
    @Schema(description = "The date when this model was created")
    private LocalDateTime createTimestamp;
    @Schema(description = "The date when this model was updated")
    private LocalDateTime updateTimestamp;
    @Schema(description = "The user that created this model")
    private UserResponseDto user;
}
