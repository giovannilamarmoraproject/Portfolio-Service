package com.giovannilamarmora.project.portfolio.PortfolioService.cache;

import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioData;
import reactor.core.publisher.Mono;

public interface CmsService {

  Mono<PortfolioData> getAndMapPortfolioStrapiData(String locale);
}
