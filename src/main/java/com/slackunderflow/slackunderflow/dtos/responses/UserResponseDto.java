package com.slackunderflow.slackunderflow.dtos.responses;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data about an user")
public class UserResponseDto {

    @Schema(description = "The username of the user")
    private String username;

    @Schema(description = "The current points of the user")
    private Integer points = 0;

    @Schema(description = "The current badge of the user")
    private BadgeEnum badge = BadgeEnum.BEGINNER;

    @Schema(description = "The jwt token for this session")
    private String jwt;
}
