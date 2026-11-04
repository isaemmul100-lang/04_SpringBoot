package org.example.jpaintro.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().components(new Components()).info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Rest API 설명서")
                .description("REST API 수업 API 명세서")
                .version("1.0.0");
    }
}