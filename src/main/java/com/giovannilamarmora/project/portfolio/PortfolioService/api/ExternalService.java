package com.giovannilamarmora.project.portfolio.PortfolioService.api;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.ContentfulClient;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.ContentfulException;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.ContentfulMapper;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.model.Contentful;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.StrapiService;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto.StrapiPortfolio;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO.CMSData;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioData;
import com.giovannilamarmora.project.portfolio.PortfolioService.cache.CmsService;
import io.github.giovannilamarmora.utils.generic.Response;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import io.github.giovannilamarmora.utils.interceptors.correlationID.CorrelationIdUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Logged
public class ExternalService implements CmsService {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());
  @Autowired private ContentfulClient contentfulClient;
  @Autowired private ContentfulMapper contentfulMapper;

  @Autowired private StrapiService strapiService;

  @LogInterceptor(type = LogTimeTracker.ActionType.EXTERNAL)
  public ResponseEntity<List<CMSData>> getAndMapCMSData(String locale) {
    ResponseEntity<Contentful> responseEntity = contentfulClient.getCMSData(locale);

    if (responseEntity.getBody() == null || responseEntity.getStatusCode().isError()) {
      LOG.error("An error occurred during getting data of contentful");
      throw new ContentfulException("An error occurred during getting data of contentful");
    }
    Contentful contentful = responseEntity.getBody();
    List<CMSData> cmsData = contentfulMapper.fromContentfulToCMSData(contentful);

    String message = "Data successfully founded!";
    Response response =
        new Response(
            HttpStatus.OK.value(), message, CorrelationIdUtils.getCorrelationId(), cmsData);
    return ResponseEntity.ok(cmsData);
  }

  @Override
  @LogInterceptor(type = LogTimeTracker.ActionType.EXTERNAL)
  public Mono<PortfolioData> getAndMapPortfolioStrapiData(String locale) {
    Mono<StrapiPortfolio> portfolioDataMono = strapiService.getPortfolioData(locale);
    Mono<List<StrapiPortfolio>> portfolioWorksMono = strapiService.getPortfolioWorks(locale);
    Mono<List<StrapiPortfolio>> portfolioCoursesMono = strapiService.getPortfolioCourses(locale);
    Mono<List<StrapiPortfolio>> portfolioProjectsMono = strapiService.getPortfolioProjects(locale);

    return Mono.zip(
            portfolioDataMono, portfolioWorksMono, portfolioCoursesMono, portfolioProjectsMono)
        .flatMap(
            tuple -> {
              StrapiPortfolio portfolioData = tuple.getT1();
              List<StrapiPortfolio> portfolioWorks = tuple.getT2();
              List<StrapiPortfolio> portfolioCourses = tuple.getT3();
              List<StrapiPortfolio> portfolioProjects = tuple.getT4();
              return Mono.just(
                  ExternalMapper.mapPortfolioData(
                      portfolioData, portfolioWorks, portfolioCourses, portfolioProjects));
            });
  }
}
