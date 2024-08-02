package com.giovannilamarmora.project.portfolio.PortfolioService.cache;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.ExternalService;
import io.github.giovannilamarmora.utils.logger.LoggerFilter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableCaching
public class CacheConfig {

  private final Logger LOG = LoggerFilter.getLogger(this.getClass());

  @Value(value = "${spring.data.redis.enabled:false}")
  private Boolean redisCacheEnabled;

  @Value(value = "${spring.data.cache.active:false}")
  private Boolean defaultCacheEnabled;

  @Autowired private RedisConnectionFactory redisConnectionFactory;

  @Bean
  @Primary
  @ConditionalOnProperty(prefix = "spring.data.redis", name = "enabled", havingValue = "true")
  public CacheManager redisCacheManager() {
    LOG.info("[CACHE] Redis Cache Manager (RedisCacheManager) Enabled");
    return RedisCacheManager.builder(redisConnectionFactory).build();
  }

  @Bean
  @Primary
  @ConditionalOnProperty(
      prefix = "spring.data.redis",
      name = "enabled",
      havingValue = "false",
      matchIfMissing = true)
  public CacheManager simpleCacheManager() {
    if (defaultCacheEnabled) LOG.info("[CACHE] Default Cache Manager (SimpleCacheManager) Enabled");
    return new ConcurrentMapCacheManager(CmsCacheService.PORTFOLIO_CACHE);
  }

  @Bean(name = "cmsService")
  @ConditionalOnProperty(prefix = "spring.data.cache", name = "active", havingValue = "true")
  public CmsService getCachedData() {
    LOG.info("[CACHE] Cache Service Active");
    if (redisCacheEnabled) {
      LOG.info("[CACHE] Using Redis Cache Service");
      return new RedisCacheService();
    }
    LOG.info("[CACHE] Using Default Cache Service");
    return new CmsCacheService();
  }

  @Bean(name = "cmsService")
  @ConditionalOnProperty(
      prefix = "spring.data.cache",
      name = "active",
      havingValue = "false",
      matchIfMissing = true)
  public CmsService getData() {
    LOG.info("[CACHE] Cache Service Disabled");
    return new ExternalService();
  }
}
