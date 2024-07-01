package com.giovannilamarmora.project.portfolio.PortfolioService.cache;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.ExternalService;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.CacheStatus;
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
import org.springframework.util.ObjectUtils;
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
  public Mono<CacheStatus> deleteCache() {
    LOG.info("[Caching] Deleting cache for {}", PORTFOLIO_CACHE);
    if (!ObjectUtils.isEmpty(cacheManager) && !cacheManager.getCacheNames().isEmpty()) {
      LOG.info("Deleting cache for {}", cacheManager.getCacheNames());
      cacheManager
          .getCacheNames()
          .forEach(
              cacheName -> {
                if (!ObjectUtils.isEmpty(cacheName)
                    && !ObjectUtils.isEmpty(cacheManager.getCache(cacheName))) {
                  Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
                }
              });
      return Mono.just(CacheStatus.OK);
    }
    return Mono.just(CacheStatus.KO);
  }
}
