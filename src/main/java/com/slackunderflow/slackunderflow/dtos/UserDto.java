package com.slackunderflow.slackunderflow.dtos;

import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(description = "Data about an user for login/register")
public class UserDto {

    public interface RegisteringUser {
    }

    @NotEmpty(message = "You need to enter a username")
    @NotEmpty(message = "You need to enter a username",
            groups = {RegisteringUser.class})
    @Size(max = 30,
            message = "Username is too long")
    @Size(max = 30,
            message = "Username is too long",
            groups = {RegisteringUser.class})
    @Schema(description = "The username of the user you want to login/register")
    private String username;

    @NotEmpty(message = "You need to enter a password")
    @Size(min = 5,
            message = "Password must have at least 5 chr",
            groups = {RegisteringUser.class})
    @Size(max = 50,
            message = "Invalid password",
            groups = {RegisteringUser.class})
    @Schema(description = "The password of the user you want to login/register")
    private String password;


}
