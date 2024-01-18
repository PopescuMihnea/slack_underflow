package com.slackunderflow.slackunderflow.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class BodyRequest {

    @NotBlank(message = "Must have a body")
    @Size(max = 2000, message = "The body is too long")
    @Schema(description = "The body of this model, contains the text that is showed in the front end")
    private String body;
}
