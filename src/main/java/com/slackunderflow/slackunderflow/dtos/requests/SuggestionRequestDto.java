package com.slackunderflow.slackunderflow.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Data about a suggestion that is to be created")
public class SuggestionRequestDto extends BodyRequest {

    @Min(value = 0, message = "Invalid answer id")
    @Schema(description = "The answer id that this suggestion is added to")
    private Long answerId;
}
