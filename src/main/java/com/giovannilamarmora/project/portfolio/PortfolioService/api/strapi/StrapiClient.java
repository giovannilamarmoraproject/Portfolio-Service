package com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto.StrapiResponse;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import io.github.giovannilamarmora.utils.logger.LoggerFilter;
import io.github.giovannilamarmora.utils.webClient.UtilsUriBuilder;
import io.github.giovannilamarmora.utils.webClient.WebClientRest;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Logged
public class StrapiClient {

  private final Logger LOG = LoggerFilter.getLogger(this.getClass());
  private final WebClientRest webClientRest = new WebClientRest();

  @Value(value = "${rest.client.strapi.baseUrl}")
  private String strapiUrl;

  @Value(value = "${rest.client.strapi.bearer}")
  private String strapiToken;

  @Value(value = "${rest.client.strapi.path.portfolioConfig}")
  private String portfolioConfigUrl;

  @Value(value = "${rest.client.strapi.path.portfolioWork}")
  private String portfolioWorkUrl;

  @Value(value = "${rest.client.strapi.path.portfolioCourse}")
  private String portfolioCourseUrl;

  @Value(value = "${rest.client.strapi.path.portfolioProject}")
  private String portfolioProjectUrl;

  @Autowired private WebClient.Builder builder;

  @PostConstruct
  void init() {
    webClientRest.setBaseUrl(strapiUrl);
    webClientRest.init(builder);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.EXTERNAL)
  public Mono<ResponseEntity<StrapiResponse>> getPortfolioData(String locale) {
    Map<String, Object> params = new HashMap<>();
    params.put("locale", locale);
    // params.put("populate", "*");

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + strapiToken);

    return webClientRest.perform(
        HttpMethod.GET,
        UtilsUriBuilder.buildUri(portfolioConfigUrl, params),
        headers,
        StrapiResponse.class);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.EXTERNAL)
  public Mono<ResponseEntity<StrapiResponse>> getPortfolioWorks(String locale) {
    Map<String, Object> params = new HashMap<>();
    params.put("locale", locale);
    params.put("sort[0]", "date_to:desc");
    // params.put("populate", "*");

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + strapiToken);

    return webClientRest.perform(
        HttpMethod.GET,
        UtilsUriBuilder.buildUri(portfolioWorkUrl, params),
        headers,
        StrapiResponse.class);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.EXTERNAL)
  public Mono<ResponseEntity<StrapiResponse>> getPortfolioCourses(String locale) {
    Map<String, Object> params = new HashMap<>();
    params.put("locale", locale);
    params.put("sort[0]", "date_to:desc");
    // params.put("populate", "*");

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + strapiToken);

    return webClientRest.perform(
        HttpMethod.GET,
        UtilsUriBuilder.buildUri(portfolioCourseUrl, params),
        headers,
        StrapiResponse.class);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.EXTERNAL)
  public Mono<ResponseEntity<StrapiResponse>> getPortfolioProjects(String locale) {
    Map<String, Object> params = new HashMap<>();
    params.put("locale", locale);
    params.put("sort[0]", "priority:desc");
    // params.put("populate", "*");

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + strapiToken);

    return webClientRest.perform(
        HttpMethod.GET,
        UtilsUriBuilder.buildUri(portfolioWorkUrl, params),
        headers,
        StrapiResponse.class);
  }
}
