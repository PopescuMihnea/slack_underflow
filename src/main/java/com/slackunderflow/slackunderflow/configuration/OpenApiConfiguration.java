package com.slackunderflow.slackunderflow.configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "QA API", version = "1.0",
        description = "API for questions and answers",
        contact = @Contact(name = "Popescu Mihnea-Valentin")),
        security = {@SecurityRequirement(name = "bearerToken")}
)
@SecuritySchemes({
        @SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP,
                scheme = "bearer", bearerFormat = "JWT")
})
public class OpenApiConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {

        var modelNotFoundErrorSchema = new Schema<Map<String, String>>()
                .name("ModelNotFoundErrorBody")
                .title("ModelNotFoundErrorBody")
                .description("Contains error for model not found")
                .addProperty("message", new StringSchema().example("Model not found with id: ").description("Data about with field was used to search model"))
                .addProperty("body", new StringSchema().example("25").description("Value of the field that was used to search the model"));

        var authorizationErrorSchema = new Schema<Map<String, String>>()
                .name("AuthorizationErrorBody")
                .title("AuthorizationErrorBody")
                .description("Contains user data in case user authorization fails (the user is not found in the database)")
                .addProperty("username", new StringSchema().example("Mihnea").description("Username of the user that tried to authenticate"))
                .addProperty("password", new StringSchema().example("").description("Empty password field"))
                .addProperty("message", new StringSchema().example("User is not found").description("Message that describes to front end that user doesn't exist"));


        return new OpenAPI()
                .components(new Components()
                        .addSchemas(modelNotFoundErrorSchema.getName(), modelNotFoundErrorSchema)
                        .addSchemas(authorizationErrorSchema.getName(), authorizationErrorSchema)
                );
    }
}