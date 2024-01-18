package com.slackunderflow.slackunderflow.dtos.responses;

import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.models.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Data about a question")
public class QuestionResponseDto extends BodyResponse {

    @Schema(description = "The title of the question")
    private String title;
    @Schema(description = "A set of topics that the question is related to, stored as enums")
    private Set<TopicEnum> topics;
}
