package com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi;

import io.github.giovannilamarmora.accesssphere.exception.ExceptionMap;
import io.github.giovannilamarmora.utils.exception.ExceptionCode;
import io.github.giovannilamarmora.utils.exception.UtilsException;

public class StrapiException extends UtilsException {

  private static final ExceptionCode DEFAULT_CODE = ExceptionMap.ERR_STRAPI_404;

  public StrapiException(String message) {
    super(DEFAULT_CODE, message);
  }

  public StrapiException(ExceptionCode exceptionCode, String message) {
    super(exceptionCode, message);
  }
}
