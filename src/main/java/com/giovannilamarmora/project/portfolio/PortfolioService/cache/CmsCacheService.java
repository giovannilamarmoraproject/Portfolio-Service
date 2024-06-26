package com.giovannilamarmora.project.portfolio.PortfolioService.cache;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.ExternalService;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioData;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.logger.LoggerFilter;
import java.util.Objects;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CmsCacheService implements CmsService {

  private static final String PORTFOLIO_CACHE = "Portfolio_Cache";

  private final Logger LOG = LoggerFilter.getLogger(this.getClass());
  @Autowired private CacheManager cacheManager;
  @Autowired private ExternalService externalService;

  @Override
  @Cacheable(value = PORTFOLIO_CACHE, key = "#locale", condition = "#locale!=null")
  @LogInterceptor(type = LogTimeTracker.ActionType.CACHE)
  public Mono<PortfolioData> getAndMapPortfolioStrapiData(String locale) {
    LOG.info("[Caching] Getting data from Strapi with locale={}", locale);
    return externalService.getAndMapPortfolioStrapiData(locale);
  }

  @Caching(evict = @CacheEvict(value = PORTFOLIO_CACHE))
  @LogInterceptor(type = LogTimeTracker.ActionType.CACHE)
  public void deleteUserCache() {
    LOG.info("[Caching] Deleting cache for {}", PORTFOLIO_CACHE);
    Objects.requireNonNull(cacheManager.getCache(PORTFOLIO_CACHE)).clear();
  }
}