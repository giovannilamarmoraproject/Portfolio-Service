package com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS;

import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.entity.CMSEntity;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CMSCacheService {
  private static final String CMS_DATA_CACHE = "CMS-Data-Cache";
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());
  @Autowired private CacheManager cacheManager;
  @Autowired private ICMSDAO cmsDao;

  @Caching(cacheable = @Cacheable(value = CMS_DATA_CACHE, key = "#locale"))
  @LogInterceptor(type = LogTimeTracker.ActionType.CACHE)
  public List<CMSEntity> findAllByLocale(String locale) {
    LOG.info("[Caching] CMSEntity into Database for locale {}", locale);
    return cmsDao.findAllByLocale(locale);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.CACHE)
  public List<CMSEntity> findAll() {
    return cmsDao.findAll();
  }

  public List<CMSEntity> saveAll(List<CMSEntity> cmsEntities) {
    deleteCache();
    return cmsDao.saveAll(cmsEntities);
  }

  public void deleteAll() {
    deleteCache();
    cmsDao.deleteAll();
  }

  @Caching(evict = {@CacheEvict(value = CMS_DATA_CACHE)})
  @LogInterceptor(type = LogTimeTracker.ActionType.CACHE)
  public void deleteCache() {
    LOG.info("[Caching] Deleting cache for {}", CMS_DATA_CACHE);
    Objects.requireNonNull(cacheManager.getCache(CMS_DATA_CACHE)).clear();
  }
}
