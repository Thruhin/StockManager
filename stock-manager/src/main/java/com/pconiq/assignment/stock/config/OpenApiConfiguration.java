package com.pconiq.assignment.stock.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfiguration {
    
    @Bean
    public GroupedOpenApi groupedOpenApi() {
      return GroupedOpenApi.builder().group("v1").pathsToMatch("/**").packagesToScan("com.pconiq.assignment.stock.controller").build();
    }

    @Bean
    public OpenAPI openAPI() {
      OpenAPI openAPI = new OpenAPI();
      openAPI.info(new Info().title("Stock Service")
          .description("Provides APIs for managing the stocks").version("1.0.0"));
          //.license(new License().name("Terms of Use").url("https://help.somesoftware.com/terms_of_use.html")));
      return openAPI;
    }

}
