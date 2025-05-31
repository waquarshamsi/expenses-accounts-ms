package com.finance.accounts.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for SpringDoc OpenAPI (Swagger).
 */
@Configuration
public class OpenApiConfig {

  /**
   * Configures the OpenAPI documentation.
   *
   * @return the OpenAPI bean
   */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
        .info(new Info()
            .title("Accounts Microservice API")
            .description("API for managing various types of financial accounts")
            .version("1.0.0")
            .contact(new Contact()
                .name("Finance Team")
                .email("finance@example.com")));
  }
}
