package com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto.StrapiPortfolio;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto.StrapiResponse;
import com.giovannilamarmora.project.portfolio.PortfolioService.exception.ExceptionMap;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.logger.LoggerFilter;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class StrapiValidator {

  private static final Logger LOG = LoggerFilter.getLogger(StrapiValidator.class);

  @LogInterceptor(type = LogTimeTracker.ActionType.VALIDATOR)
  public static StrapiPortfolio validateAndReturnStrapiPortfolio(
      ResponseEntity<StrapiResponse> strapiResponseResponseEntity) {
    if (!strapiResponseResponseEntity.hasBody()
        || ObjectUtils.isEmpty(strapiResponseResponseEntity.getBody())
        || strapiResponseResponseEntity.getBody().getData().isEmpty()) {
      LOG.error("An error happen during get portfolio data on strapi, data not found");
      throw new StrapiException(
          ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
    }
    return strapiResponseResponseEntity.getBody().getData().getFirst().getAttributes();
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.VALIDATOR)
  public static List<StrapiPortfolio> validateAndReturnStrapiPortfolioList(
      ResponseEntity<StrapiResponse> strapiResponseResponseEntity) {
    if (!strapiResponseResponseEntity.hasBody()
        || ObjectUtils.isEmpty(strapiResponseResponseEntity.getBody())
        || strapiResponseResponseEntity.getBody().getData().isEmpty()) {
      LOG.error("An error happen during get portfolio data on strapi, data not found");
      throw new StrapiException(
          ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
    }
    return strapiResponseResponseEntity.getBody().getData().stream()
        .map(StrapiResponse.StrapiData::getAttributes)
        .toList();
  }
}
