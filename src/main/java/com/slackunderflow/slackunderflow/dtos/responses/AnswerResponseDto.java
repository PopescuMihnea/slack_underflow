package com.slackunderflow.slackunderflow.dtos.responses;

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
@Schema(description = "Data about an answer")
public class AnswerResponseDto extends BodyResponse {

    @Schema(description = "The rank of this answer between int 1-3, given by the user who made the question")
    private Integer rank;
    @Schema(description = "The question that this answer is added to")
    private Question question;
}
