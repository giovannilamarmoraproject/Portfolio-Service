package com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO.CMSData;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.entity.CMSEntity;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CMSMapper {

  private final ObjectMapper mapper =
      new ObjectMapper()
          .findAndRegisterModules()
          .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_MAPPER)
  public List<CMSData> fromCMSEntitiesToCMSDataList(List<CMSEntity> cmsEntities) {
    return cmsEntities.stream()
        .map(
            cmsEntity -> {
              CMSData cmsData = new CMSData();
              BeanUtils.copyProperties(cmsEntity, cmsData);
                try {
                    cmsData.setData(
                        mapper.readValue(
                            cmsEntity.getData(), new TypeReference<Map<String, Object>>() {}));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return cmsData;
            })
        .collect(Collectors.toList());
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_MAPPER)
  public List<CMSEntity> fromCMSDataListToCMSEntities(List<CMSData> cmsDataList, String locale) {
    return cmsDataList.stream()
        .map(
            cmsData -> {
              CMSEntity cmsEntity = new CMSEntity();
              BeanUtils.copyProperties(cmsData, cmsEntity);
                try {
                    cmsEntity.setData(mapper.writeValueAsString(cmsData.getData()));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                cmsEntity.setLocale(locale);
              return cmsEntity;
            })
        .collect(Collectors.toList());
  }
}
