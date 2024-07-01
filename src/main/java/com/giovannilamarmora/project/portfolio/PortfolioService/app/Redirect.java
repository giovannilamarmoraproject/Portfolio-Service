package com.giovannilamarmora.project.portfolio.PortfolioService.app;

import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import io.github.giovannilamarmora.utils.web.WebManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Logged
@RestController
@CrossOrigin(origins = "*")
public class Redirect {

  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  @GetMapping(value = "/")
  @Tag(name = "App", description = "API GET Portfolio")
  @Operation(description = "API GET Portfolio", tags = "App")
  @LogInterceptor(type = LogTimeTracker.ActionType.CONTROLLER)
  public Mono<ResponseEntity<String>> redirectToSite(ServerHttpRequest request) {
    LOG.info("Redirect ({}) to website", WebManager.getAddressFromRequest(request));
    return Mono.just(
        ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
            .location(URI.create("https://giovannilamarmora.github.io"))
            .build());
  }
}
