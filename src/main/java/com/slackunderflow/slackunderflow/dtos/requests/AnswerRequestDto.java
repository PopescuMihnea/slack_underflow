package com.slackunderflow.slackunderflow.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Data about an answer that is to be created")
public class AnswerRequestDto extends BodyRequest {

    @Min(value = 0, message = "Invalid question id")
    @Schema(description = "The question id that this answer is added to")
    private Long questionId;
}
