package com.giovannilamarmora.project.portfolio.PortfolioService.app;

import com.giovannilamarmora.project.portfolio.PortfolioService.cache.CmsCacheService;
import com.giovannilamarmora.project.portfolio.PortfolioService.cache.CmsService;
import io.github.giovannilamarmora.utils.generic.Response;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import io.github.giovannilamarmora.utils.interceptors.correlationID.CorrelationIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Logged
public class AppService {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  @Autowired private CmsCacheService cmsCacheService;
  @Autowired private CmsService cmsService;

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<ResponseEntity<Response>> getPortfolioData(String locale) {
    LOG.info("\uD83D\uDCD2 Getting Portfolio Data for {}", locale);

    return cmsService
        .getAndMapPortfolioStrapiData(locale)
        .map(
            portfolioData -> {
              String message = "Data successfully founded for " + locale + "!";
              Response response =
                  new Response(
                      HttpStatus.OK.value(),
                      message,
                      CorrelationIdUtils.getCorrelationId(),
                      portfolioData);
              return ResponseEntity.ok(response);
            })
        .doOnSuccess(
            responseResponseEntity ->
                LOG.info("\uD83D\uDCD2 Ending Getting Portfolio Data for {}", locale));
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<ResponseEntity<Response>> deleteCache() {
    LOG.info("\uD83D\uDCD2 Deleting cache");

    return cmsCacheService
        .deleteCache()
        .flatMap(
            portfolioData -> {
              String message = "Cache successfully deleted!";
              Response response =
                  new Response(
                      HttpStatus.OK.value(), message, CorrelationIdUtils.getCorrelationId(), null);
              return Mono.just(ResponseEntity.ok(response));
            })
        .doOnSuccess(responseResponseEntity -> LOG.info("\uD83D\uDCD2 Ending Deleting cache"));
  }
}
