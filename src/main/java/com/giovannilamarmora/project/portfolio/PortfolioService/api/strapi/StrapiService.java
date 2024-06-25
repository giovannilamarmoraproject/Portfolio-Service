package com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto.StrapiPortfolio;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StrapiService {

  @Autowired private StrapiClient strapiClient;

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiPortfolio> getPortfolioData(String locale) {
    return strapiClient
        .getPortfolioData(locale)
        .map(StrapiValidator::validateAndReturnStrapiPortfolio)
        .doOnError(StrapiException::validateAndReturnError);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<List<StrapiPortfolio>> getPortfolioWorks(String locale) {
    return strapiClient
        .getPortfolioWorks(locale)
        .map(StrapiValidator::validateAndReturnStrapiPortfolioList)
        .doOnError(StrapiException::validateAndReturnError);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<List<StrapiPortfolio>> getPortfolioCourses(String locale) {
    return strapiClient
        .getPortfolioCourses(locale)
        .map(StrapiValidator::validateAndReturnStrapiPortfolioList)
        .doOnError(StrapiException::validateAndReturnError);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<List<StrapiPortfolio>> getPortfolioProjects(String locale) {
    return strapiClient
        .getPortfolioProjects(locale)
        .map(StrapiValidator::validateAndReturnStrapiPortfolioList)
        .doOnError(StrapiException::validateAndReturnError);
  }
}
