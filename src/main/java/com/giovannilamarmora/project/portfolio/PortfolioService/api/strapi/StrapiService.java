package com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto.StrapiResponse;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.logger.LoggerFilter;
import io.github.giovannilamarmora.utils.utilities.MapperUtils;
import io.github.giovannilamarmora.utils.utilities.Utilities;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

@Service
public class StrapiService {

  private final Logger LOG = LoggerFilter.getLogger(this.getClass());
  @Autowired private StrapiClient strapiClient;

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiResponse> registerUserToStrapi(User user, ClientCredential clientCredential) {
    StrapiUser strapiUser = StrapiMapper.mapFromUserToStrapiUser(user, clientCredential);
    return strapiClient
        .saveUser(strapiUser)
        .map(
            strapiResponseResponseEntity -> {
              DataValidator.validateStrapiResponse(strapiResponseResponseEntity);
              strapiResponseResponseEntity
                  .getBody()
                  .getUser()
                  .setApp_roles(strapiUser.getApp_roles());
              return strapiResponseResponseEntity.getBody();
            })
        .doOnError(
            throwable -> {
              String messageBody = throwable.getMessage().split("and body message ")[1];
              if (Utilities.isInstanceOf(messageBody, new TypeReference<StrapiError>() {})) {
                StrapiError response;
                try {
                  response = Utils.mapper().readValue(messageBody, StrapiError.class);
                } catch (JsonProcessingException e) {
                  LOG.error(
                      "An error happen during read value from strapi, message is {}",
                      e.getMessage());
                  throw new OAuthException(
                      ExceptionMap.ERR_STRAPI_500, ExceptionMap.ERR_STRAPI_500.getMessage());
                }
                if (response
                    .getError()
                    .getMessage()
                    .equalsIgnoreCase("Email or Username are already taken")) {
                  LOG.error(
                      "An error happen during registration on strapi, message is {}",
                      response.getError().getMessage());
                  throw new OAuthException(
                      ExceptionMap.ERR_OAUTH_400, response.getError().getMessage());
                }
              }
            });
  }

  /**
   * Ritorno l'utente filtrato, se non trovo gli utenti torno NOT_FOUND
   *
   * @param email
   * @return
   */
  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiUser> getUserByEmail(String email) {
    return strapiClient
        .getUserByEmail(email)
        .flatMap(
            listResponseEntity -> {
              if (!listResponseEntity.hasBody()
                  || ObjectUtils.isEmpty(listResponseEntity.getBody())) {
                LOG.error("An error happen during get user on strapi, user not found");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
              }
              if (listResponseEntity.getBody().isEmpty()) {
                LOG.error("An error happen during get user on strapi, user not found");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
              }
              return Mono.just(listResponseEntity.getBody().getFirst());
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiUser> getUserByIdentifier(String identifier) {
    return strapiClient
        .getUserByIdentifier(identifier)
        .flatMap(
            listResponseEntity -> {
              if (!listResponseEntity.hasBody()
                  || ObjectUtils.isEmpty(listResponseEntity.getBody())) {
                LOG.error(
                    "An error happen during get user by identifier on strapi, user not found");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
              }
              if (listResponseEntity.getBody().isEmpty()) {
                LOG.error(
                    "An error happen during get user by identifier on strapi, user not found");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
              }
              return Mono.just(listResponseEntity.getBody().getFirst());
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<List<StrapiUser>> getUsers() {
    return strapiClient
        .getUsers()
        .map(
            listResponseEntity -> {
              if (!listResponseEntity.hasBody()
                  || ObjectUtils.isEmpty(listResponseEntity.getBody())) {
                LOG.error("An error happen during get users on strapi, user not found");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
              }
              if (listResponseEntity.getBody().isEmpty()) {
                LOG.error("An error happen during get users on strapi, user not found");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
              }
              return listResponseEntity.getBody();
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiUser> getUserByTokenReset(String tokenReset) {
    return strapiClient
        .getUserByTokenReset(tokenReset)
        .flatMap(
            listResponseEntity -> {
              if (!listResponseEntity.hasBody()
                  || ObjectUtils.isEmpty(listResponseEntity.getBody())) {
                LOG.error(
                    "An error happen during get user by tokenReset on strapi, user not found");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
              }
              if (listResponseEntity.getBody().isEmpty()) {
                LOG.error(
                    "An error happen during get user by tokenReset on strapi, user not found");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_404, ExceptionMap.ERR_STRAPI_404.getMessage());
              }
              StrapiUser strapiUser = listResponseEntity.getBody().getFirst();
              if (strapiUser.getUpdatedAt().plusDays(1).isBefore(LocalDateTime.now())) {
                LOG.error("Token Expired, last created was at {}", strapiUser.getUpdatedAt());
                throw new OAuthException(
                    ExceptionMap.ERR_OAUTH_403, ExceptionMap.ERR_OAUTH_403.getMessage());
              }
              return Mono.just(listResponseEntity.getBody().getFirst());
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiResponse> login(String email, String password) {
    return Mono.zip(
            strapiClient.login(new StrapiLogin(email, password)),
            strapiClient.getRefreshToken(new StrapiLogin(email, password)))
        .flatMap(
            objects -> {
              ResponseEntity<StrapiResponse> strapiResponseResponseEntity = objects.getT1();
              if (ObjectUtils.isEmpty(strapiResponseResponseEntity.getBody())) {
                LOG.error("Strapi returned an empty body on login");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_400, ExceptionMap.ERR_STRAPI_400.getMessage());
              }
              ResponseEntity<StrapiResponse> strapiResponseRefresh = objects.getT2();
              if (ObjectUtils.isEmpty(strapiResponseRefresh.getBody())) {
                LOG.error("Strapi returned an empty body on login");
                throw new OAuthException(
                    ExceptionMap.ERR_STRAPI_400, ExceptionMap.ERR_STRAPI_400.getMessage());
              }
              strapiResponseResponseEntity
                  .getBody()
                  .setRefresh_token(strapiResponseRefresh.getBody().getRefresh_token());
              return Mono.just(strapiResponseResponseEntity.getBody());
            })
        .doOnError(
            throwable -> {
              if (throwable.getMessage().contains("Invalid identifier or password")) {
                LOG.error("Username o password are wrong, error is {}", throwable.getMessage());
                throw new OAuthException(
                    ExceptionMap.ERR_OAUTH_401, ExceptionMap.ERR_OAUTH_401.getMessage());
              }
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiUser> userInfo(String token) {
    return strapiClient
        .userInfo(token)
        .flatMap(
            strapiUserResponseEntity -> {
              if (ObjectUtils.isEmpty(strapiUserResponseEntity.getBody())) {
                LOG.error("Strapi returned an empty body on userInfo");
                throw new OAuthException(
                    ExceptionMap.ERR_OAUTH_401, ExceptionMap.ERR_OAUTH_401.getMessage());
              }
              return Mono.just(strapiUserResponseEntity.getBody());
            })
        .doOnError(
            throwable -> {
              if (throwable.getMessage().contains("Missing or invalid credentials")) {
                LOG.error("Basic token is wrong or not valid, error is {}", throwable.getMessage());
                throw new OAuthException(
                    ExceptionMap.ERR_OAUTH_401, ExceptionMap.ERR_OAUTH_401.getMessage());
              }
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiResponse> refreshJWToken(String refresh_token) {
    return strapiClient
        .refreshToken(refresh_token)
        .flatMap(
            strapiUserResponseEntity -> {
              if (ObjectUtils.isEmpty(strapiUserResponseEntity.getBody())) {
                LOG.error("Strapi returned an empty body on refreshToken");
                throw new OAuthException(
                    ExceptionMap.ERR_OAUTH_401, ExceptionMap.ERR_OAUTH_401.getMessage());
              }
              strapiUserResponseEntity.getBody().setRefresh_token(refresh_token);
              return Mono.just(strapiUserResponseEntity.getBody());
            })
        .doOnError(
            throwable -> {
              if (throwable.getMessage().contains("Refresh Token not found")) {
                LOG.error(
                    "Refresh token is wrong or not valid, error is {}", throwable.getMessage());
                throw new OAuthException(
                    ExceptionMap.ERR_OAUTH_401, ExceptionMap.ERR_OAUTH_401.getMessage());
              }
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiUser> updateUser(User user) {
    StrapiUser strapiUser = StrapiMapper.mapFromUserToStrapiUser(user, null);
    return strapiClient
        .updateUser(strapiUser)
        .flatMap(
            strapiUserResponseEntity -> {
              if (ObjectUtils.isEmpty(strapiUserResponseEntity.getBody())) {
                LOG.error("Strapi returned an empty body on updateUser");
                throw new OAuthException(
                    ExceptionMap.ERR_OAUTH_401, ExceptionMap.ERR_OAUTH_401.getMessage());
              }
              return Mono.just(strapiUserResponseEntity.getBody());
            })
        .doOnError(
            throwable -> {
              if (throwable.getMessage().contains("NotFoundError")) {
                LOG.error("User not found, error is {}", throwable.getMessage());
                throw new OAuthException(
                    ExceptionMap.ERR_OAUTH_401, ExceptionMap.ERR_OAUTH_401.getMessage());
              }
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<StrapiEmailTemplate> getTemplateById(String templateId, String locale) {
    return strapiClient
        .getTemplateById(templateId, locale)
        .flatMap(
            responseEntity -> {
              if (ObjectUtils.isEmpty(responseEntity.getBody())
                  || ObjectUtils.isEmpty(responseEntity.getBody().getData())) {
                LOG.error("Strapi returned an empty object");
                throw new StrapiException(
                    ExceptionMap.ERR_STRAPI_404,
                    "Template with templateId (" + templateId + ") not found");
              }
              ObjectMapper mapper = MapperUtils.mapper().failOnEmptyBean().build();
              return Mono.just(
                  mapper.convertValue(
                      responseEntity.getBody().getData().getFirst().getAttributes(),
                      StrapiEmailTemplate.class));
            });
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.SERVICE)
  public Mono<ResponseEntity<Void>> logout(String refresh_token) {
    return strapiClient
        .logout(refresh_token)
        .doOnSuccess(o -> LOG.info("Strapi logout successfully!"));
  }
}
