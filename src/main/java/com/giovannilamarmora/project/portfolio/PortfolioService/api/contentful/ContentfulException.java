package com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful;

import com.giovannilamarmora.project.portfolio.PortfolioService.exception.ExceptionMap;
import io.github.giovannilamarmora.utils.exception.ExceptionCode;
import io.github.giovannilamarmora.utils.exception.UtilsException;

public class ContentfulException extends UtilsException {
    private static final ExceptionCode DEFAULT_CODE = ExceptionMap.ERR_CONT_DATA_001;

    public ContentfulException(String message) {
        super(DEFAULT_CODE, message);
    }

    public ContentfulException(String message, String exceptionMessage) {
        super(DEFAULT_CODE, message, exceptionMessage);
    }
}
