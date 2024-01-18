package com.slackunderflow.slackunderflow.dtos.requests;

import com.slackunderflow.slackunderflow.enums.TopicEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Schema(description = "Data about a question that is to be created")
public class QuestionRequestDto extends BodyRequest {

    @NotBlank(message = "Must have a title")
    @Size(min = 5,
            message = "Enter a more descriptive(longer) title")
    @Size(max = 100, message = "The title is too long")
    @Schema(description = "The title of the question")
    private String title;

    @NotEmpty(message = "Question must have topics")
    @Schema(description = "A set of topics that the question is related to, stored as enums and entered as number from 0-4")
    private Set<TopicEnum> topics;
}
