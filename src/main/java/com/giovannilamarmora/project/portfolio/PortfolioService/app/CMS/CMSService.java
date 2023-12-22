package com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS;

import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO.CMSData;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.entity.CMSEntity;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Logged
public class CMSService {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());
  @Autowired private CMSCacheService cmsCacheService;
  @Autowired private CMSMapper cmsMapper;

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_SERVICE)
  public List<CMSData> getCMSData(String locale) {
    List<CMSEntity> cmsEntities = cmsCacheService.findAllByLocale(locale);
    if (cmsEntities == null || cmsEntities.isEmpty()) {
      LOG.error("Empty Data list");
      // throw new CMSException("An error occurred during get Data from Database");
      return new ArrayList<>();
    }
    return cmsMapper.fromCMSEntitiesToCMSDataList(cmsEntities);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_SERVICE)
  public List<CMSData> getCMSDataList() {
    List<CMSEntity> cmsEntities = cmsCacheService.findAll();
    if (cmsEntities == null || cmsEntities.isEmpty()) {
      LOG.error("Empty Data list");
      // throw new CMSException("An error occurred during get Data from Database");
      return new ArrayList<>();
    }
    return cmsMapper.fromCMSEntitiesToCMSDataList(cmsEntities);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_SERVICE)
  public void saveCMSDataList(List<CMSData> cmsData, String locale) {
    if (cmsData == null || cmsData.isEmpty()) {
      LOG.error("An error occurred during save Data into Database, cmsData is empty");
      throw new CMSException(
          "An error occurred during save Data into Database, cmsData cannot be empty");
    }
    List<CMSEntity> cmsEntities = cmsMapper.fromCMSDataListToCMSEntities(cmsData, locale);
    cmsCacheService.saveAll(cmsEntities);
    LOG.info("CMSData successfully saved!");
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_SERVICE)
  public void deleteCMSData() {
    cmsCacheService.deleteAll();
    LOG.info("CMSData successfully deleted!");
  }
}
