package com.giovannilamarmora.project.portfolio.PortfolioService.api;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.StrapiService;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto.StrapiPortfolio;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.CacheStatus;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioData;
import com.giovannilamarmora.project.portfolio.PortfolioService.cache.CmsService;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Logged
public class ExternalService implements CmsService {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  @Autowired private StrapiService strapiService;

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

  @Override
  public Mono<CacheStatus> deleteCache() {
    return Mono.just(CacheStatus.OK);
  }
}
