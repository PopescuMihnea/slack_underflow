package com.slackunderflow.slackunderflow.dtos;

import com.slackunderflow.slackunderflow.enums.TopicEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Data about a topic for either creation or response")
public class TopicDto {
    private TopicEnum topic;

}
