package com.giovannilamarmora.project.portfolio.PortfolioService.exception;

import io.github.giovannilamarmora.utils.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum ExceptionMap implements ExceptionCode {
    // CMS
    ERR_CMS_DATA_001("CMS_NOT_FOUND_EXCEPTION", HttpStatus.NOT_FOUND, "Data not founded!"),
    // Contentful
    ERR_CONT_DATA_001("CONTENTFUL_EXCEPTION", HttpStatus.BAD_REQUEST, "Data errors");

    private final HttpStatus status;
    private final String message;
    private final String exceptionName;

    ExceptionMap(String exceptionName, HttpStatus status, String message) {
        this.exceptionName = exceptionName;
        this.status = status;
        this.message = message;
    }

    @Override
    public String exceptionName() {
        return this.exceptionName;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }
}