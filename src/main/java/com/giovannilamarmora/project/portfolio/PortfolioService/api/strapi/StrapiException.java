package com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto.StrapiError;
import com.giovannilamarmora.project.portfolio.PortfolioService.exception.ExceptionMap;
import io.github.giovannilamarmora.utils.exception.ExceptionCode;
import io.github.giovannilamarmora.utils.exception.UtilsException;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.utilities.MapperUtils;
import io.github.giovannilamarmora.utils.utilities.Utilities;
import org.springframework.http.HttpStatus;

public class StrapiException extends UtilsException {

  private static final ExceptionCode DEFAULT_CODE = ExceptionMap.ERR_STRAPI_404;

  public StrapiException(String message) {
    super(DEFAULT_CODE, message);
  }

  public StrapiException(ExceptionCode exceptionCode, String message) {
    super(exceptionCode, message);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.VALIDATOR)
  public static void validateAndReturnError(Throwable throwable) {
    String messageBody = throwable.getMessage().split("and body message ")[1];
    if (Utilities.isInstanceOf(messageBody, new TypeReference<StrapiError>() {})) {
      StrapiError response;
      try {
        response = MapperUtils.mapper().build().readValue(messageBody, StrapiError.class);
      } catch (JsonProcessingException e) {
        LOG.error("An error happen during read value from strapi, message is {}", e.getMessage());
        throw new StrapiException(
            ExceptionMap.ERR_STRAPI_500, ExceptionMap.ERR_STRAPI_500.getMessage());
      }
      if (response.getError().getStatus().equals(HttpStatus.NOT_FOUND.value())) {
        LOG.error(
            "An error happen during registration on strapi, message is {}",
            response.getError().getMessage());
        throw new StrapiException(ExceptionMap.ERR_STRAPI_404, response.getError().getMessage());
      }
    }
  }
}
