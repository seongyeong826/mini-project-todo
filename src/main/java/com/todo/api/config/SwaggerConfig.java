package com.todo.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription("Todo server");

        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("bearerAuth");

        return new OpenAPI()
            .info(apiInfo())
            .servers(List.of(server))
            .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
            .addSecurityItem(securityRequirement);
    }

    private Info apiInfo() {
        return new Info()
            .title("Todo server API")
            .description("Todo server API")
            .version("1.0.0");
    }

}
