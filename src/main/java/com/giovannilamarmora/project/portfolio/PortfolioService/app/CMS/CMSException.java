package com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS;

import com.giovannilamarmora.project.portfolio.PortfolioService.exception.ExceptionMap;
import io.github.giovannilamarmora.utils.exception.ExceptionCode;
import io.github.giovannilamarmora.utils.exception.UtilsException;

public class CMSException extends UtilsException {
    private static final ExceptionCode DEFAULT_CODE = ExceptionMap.ERR_CMS_DATA_001;

    public CMSException(String message) {
        super(DEFAULT_CODE, message);
    }

    public CMSException(String message, String exceptionMessage) {
        super(DEFAULT_CODE, message, exceptionMessage);
    }
}
