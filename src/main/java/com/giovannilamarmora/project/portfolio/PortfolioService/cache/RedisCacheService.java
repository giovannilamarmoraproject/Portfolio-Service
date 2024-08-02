package com.giovannilamarmora.project.portfolio.PortfolioService.cache;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.ExternalService;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.CacheStatus;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioData;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.logger.LoggerFilter;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

@Component
public class RedisCacheService implements CmsService {

  @Value(value = "${spring.data.redis.cron}")
  private String cronReset;

  private static final String PORTFOLIO_CACHE = "Portfolio_Cache";

  private final Logger LOG = LoggerFilter.getLogger(this.getClass());
  @Autowired private ExternalService externalService;
  @Autowired private ReactiveRedisTemplate<String, PortfolioData> portfolioDataTemplate;

  @Override
  @LogInterceptor(type = LogTimeTracker.ActionType.CACHE)
  public Mono<PortfolioData> getAndMapPortfolioStrapiData(String locale) {
    return portfolioDataTemplate
        .opsForValue()
        .get(getPortfolioKey(locale))
        .switchIfEmpty(
            externalService
                .getAndMapPortfolioStrapiData(locale)
                .doOnSuccess(
                    portfolioData ->
                        LOG.info("[Caching] Getting data from Strapi with locale={}", locale)))
        .flatMap(
            portfolioData ->
                portfolioDataTemplate
                    .opsForValue()
                    .set(
                        getPortfolioKey(locale),
                        portfolioData,
                        ObjectUtils.isEmpty(cronReset)
                            ? Duration.ofMinutes(60)
                            : getDurationUntilNextExecution(cronReset))
                    .thenReturn(portfolioData))
        .onErrorResume(
            throwable -> {
              LOG.error(
                  "[Caching] Error on Redis, cache disabled, message is {}",
                  throwable.getMessage());
              return externalService.getAndMapPortfolioStrapiData(locale);
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.CACHE)
  public Mono<CacheStatus> deleteCache() {
    return portfolioDataTemplate
        .getConnectionFactory()
        .getReactiveConnection()
        .serverCommands()
        .flushDb()
        .doOnSuccess(unused -> LOG.info("[Caching] Deleted Redis cache for Portfolio Data"))
        .then(Mono.just(CacheStatus.OK))
        .onErrorReturn(CacheStatus.KO);
  }

  private static String getPortfolioKey(String locale) {
    return PORTFOLIO_CACHE.concat("_").concat(locale);
  }

  private static Duration getDurationUntilNextExecution(String cronExpression) {
    CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING);
    CronParser parser = new CronParser(cronDefinition);
    Cron cron = parser.parse(cronExpression);
    ExecutionTime executionTime = ExecutionTime.forCron(cron);

    ZonedDateTime now = ZonedDateTime.now();
    Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);

    if (nextExecution.isPresent()) {
      return Duration.between(now, nextExecution.get());
    } else {
      throw new IllegalStateException("Unable to calculate the next execution time.");
    }
  }
}
