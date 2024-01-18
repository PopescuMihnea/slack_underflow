package com.slackunderflow.slackunderflow.dtos.responses;

import com.slackunderflow.slackunderflow.models.Answer;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Data about a suggestion")
public class SuggestionResponseDto extends BodyResponse {
    @Schema(description = "The answer that this suggestion is added to")
    private Answer answer;
}
