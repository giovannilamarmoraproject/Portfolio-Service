package com.giovannilamarmora.project.portfolio.PortfolioService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = "io.github.giovannilamarmora.utils")
@Configuration
@EnableScheduling
@EnableCaching
@OpenAPIDefinition(
    info = @Info(title = "Access Sphere Swagger", version = "1.0.0"),
    security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)},
    servers = {
      @Server(
          url = "https://access.sphere.service.stg.giovannilamarmora.com/",
          description = "Staging Server URL"),
      @Server(url = "http://localhost:8080", description = "Local Server URL")
    })
@SecurityScheme(
    type = SecuritySchemeType.APIKEY,
    name = HttpHeaders.AUTHORIZATION,
    in = SecuritySchemeIn.HEADER)
public class AppConfig {}
