package com.giovannilamarmora.project.portfolio.PortfolioService.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.AppService;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.CMSException;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.CMSService;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO.CMSData;
import com.giovannilamarmora.project.portfolio.PortfolioService.generic.Response;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class CronCMSData {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());
  private final ObjectMapper mapper =
      new ObjectMapper()
          .findAndRegisterModules()
          .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

  @Value(value = "#{new Boolean(${rest.scheduled.CMS.active:false})}")
  private Boolean isSchedulerActive;

  @Value(value = "${rest.scheduled.CMS.locale}")
  private String locale;

  @Autowired private AppService appService;

  @Autowired private CMSService cmsService;

  @PostConstruct
  public void scheduleAtBoot() {
    LOG.info("Scheduling at application startup");
    scheduleAllCMSData();
  }

  @Scheduled(cron = "${rest.scheduled.CMS.cron}")
  @LogInterceptor(type = LogTimeTracker.ActionType.APP_SCHEDULER)
  public void scheduleAllCMSData() {
    LOG.info("Scheduler Started at {}", LocalDateTime.now());

    if (!isSchedulerActive) {
      LOG.info("Scheduler Active status is NOT-ACTIVE, Stopping Scheduler");
      return;
    }

    List<String> locales = List.of(locale.split(","));

    if (locales.isEmpty()) {
      LOG.info("No Locale found on Database, Stopping Scheduler");
      return;
    }

    // Mi salvo tutti i Data presenti a DB in caso di rollback
    ResponseEntity<Response> allCMSData = appService.getCMSDataList();

    // Cancello tutti i dati dalla tabella MarketData
    cmsService.deleteCMSData();
    AtomicInteger index = new AtomicInteger(0);

    try {
      locales.forEach(
          local -> {
            LOG.info("Getting and Saving CMSData for locale {}", local);
            List<CMSData> getCMSData = new ArrayList<>();

            getCMSData = appService.getContentfulData(local);
            LOG.info("Found {} data of Contentful Data", getCMSData.size());
            appService.saveCMSDataList(getCMSData, local);

            if (index.getAndIncrement() != locales.size() - 1) threadSeep();
          });
    } catch (Exception e) {
      LOG.error(
          "Transaction is rolling back cause an error happen during getting Contentful for locale {}",
          locales.get(index.get()));
      LOG.error("The exception message is {}", e.getMessage());
      LOG.error("Cleaning CMSData Database");
      rollBackCMSData(locales, allCMSData);
      return;
    }
    LOG.info("Scheduler Finished at {}", LocalDateTime.now());
  }

  private void rollBackCMSData(List<String> locale, ResponseEntity<Response> allCMSData) {
    List<CMSData> cmsData =
        mapper.convertValue(
            Objects.requireNonNull(allCMSData.getBody()).getData(),
            new TypeReference<List<CMSData>>() {});
    cmsService.deleteCMSData();
    locale.forEach(
        fc -> {
          LOG.info("Found {} data of CMS Data to RollBack", cmsData.size());
          if (!cmsData.isEmpty())
            appService.saveCMSDataList(
                cmsData.stream()
                    .filter(cms -> cms.getLocale().equalsIgnoreCase(fc))
                    .collect(Collectors.toList()),
                fc);
        });
  }


  private void threadSeep() {
    try {
      LOG.info("Thread is sleeping for {} millisecond", 10000);
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      LOG.info("An error occurred during sleeping thread");
      throw new CMSException("An error occurred during sleeping thread");
    }
  }
}
