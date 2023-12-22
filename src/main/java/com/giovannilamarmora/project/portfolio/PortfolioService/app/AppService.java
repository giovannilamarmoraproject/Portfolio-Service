package com.giovannilamarmora.project.portfolio.PortfolioService.app;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.ExternalService;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.CMSException;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.CMSService;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO.CMSData;
import com.giovannilamarmora.project.portfolio.PortfolioService.generic.Response;
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

import java.util.List;

@Service
@Logged
public class AppService {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());
  @Autowired private CMSService cmsService;
  @Autowired private ExternalService externalService;

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_SERVICE)
  public ResponseEntity<Response> getCMSData(String locale) {
    LOG.info("Getting CMS Data for {}", locale);
    List<CMSData> cmsData = cmsService.getCMSData(locale);

    String message = "Data successfully founded for " + locale + "!";
    Response response =
            new Response(
                    HttpStatus.OK.value(), message, CorrelationIdUtils.getCorrelationId(), cmsData);
    return ResponseEntity.ok(response);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_SERVICE)
  public ResponseEntity<Response> getCMSDataList() {
    List<CMSData> cmsData = cmsService.getCMSDataList();

    String message = "Data successfully founded!";
    Response response =
            new Response(
                    HttpStatus.OK.value(), message, CorrelationIdUtils.getCorrelationId(), cmsData);
    return ResponseEntity.ok(response);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_SERVICE)
  public List<CMSData> getContentfulData(String locale) {
    ResponseEntity<List<CMSData>> cmsData = externalService.getAndMapCMSData(locale);

    if (!cmsData.hasBody() || cmsData.getStatusCode().isError()) {
      LOG.error("An error occurred during getting data from Contentful");
      throw new CMSException("An error occurred during getting data from Contentful");
    }
    return cmsData.getBody();
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_SERVICE)
  public void saveCMSDataList(List<CMSData> cmsData, String locale) {
    if (cmsData == null || cmsData.isEmpty()) {
      LOG.error("Empty data");
      throw new CMSException("Empty Data");
    }
    cmsService.saveCMSDataList(cmsData, locale);
  }
}
