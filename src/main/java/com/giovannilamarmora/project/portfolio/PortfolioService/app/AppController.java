package com.giovannilamarmora.project.portfolio.PortfolioService.app;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.ExternalService;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO.CMSData;
import com.giovannilamarmora.project.portfolio.PortfolioService.generic.Response;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Logged
@RestController
@RequestMapping("/v1/app")
@CrossOrigin(origins = "*")
public class AppController {
  @Autowired private AppService appService;
  @Autowired private ExternalService externalService;

  @GetMapping(value = "/CMS/data", produces = MediaType.APPLICATION_JSON_VALUE)
  @Tag(name = "App", description = "API GET CMS Data")
  @Operation(description = "API GET CMS Data", tags = "App")
  @LogInterceptor(type = LogTimeTracker.ActionType.APP_CONTROLLER)
  public ResponseEntity<Response> getData(@RequestParam(value = "locale") String locale) {
    return appService.getCMSData(locale);
  }
}
