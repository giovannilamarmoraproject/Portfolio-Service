package com.giovannilamarmora.project.portfolio.PortfolioService.app;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.ExternalService;
import io.github.giovannilamarmora.utils.generic.Response;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Logged
@RestController
@RequestMapping("/v1/app")
@CrossOrigin(origins = "*")
public class AppController {
  @Autowired private AppService appService;
  @Autowired private ExternalService externalService;

  @GetMapping(value = "/portfolio/data", produces = MediaType.APPLICATION_JSON_VALUE)
  @Tag(name = "App", description = "API GET Portfolio Data")
  @Operation(description = "API GET Portfolio Data", tags = "App")
  @LogInterceptor(type = LogTimeTracker.ActionType.CONTROLLER)
  public Mono<ResponseEntity<Response>> getPortfolioData(
      @RequestParam(value = "locale") String locale) {
    return appService.getPortfolioData(locale);
  }

  @DeleteMapping(value = "/cache/evict", produces = MediaType.APPLICATION_JSON_VALUE)
  @Tag(name = "App", description = "API Delete cache")
  @Operation(description = "API Delete cache", tags = "App")
  @LogInterceptor(type = LogTimeTracker.ActionType.CONTROLLER)
  public Mono<ResponseEntity<Response>> evictCache() {
    return appService.deleteCache();
  }
}
