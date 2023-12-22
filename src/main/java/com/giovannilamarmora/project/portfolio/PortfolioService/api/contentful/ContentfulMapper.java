package com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.model.Contentful;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO.CMSData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContentfulMapper {
  public List<CMSData> fromContentfulToCMSData(Contentful contentful) {
    List<CMSData> data = new ArrayList<>();
    contentful
        .getItems()
        .forEach(
            item -> {
              Map<String, Object> cmsData = new HashMap<>();
              item.getFields()
                  .keySet()
                  .forEach(
                      itemKey -> {
                        if (item.getFields().get(itemKey).get("content") != null
                            && !item.getFields().get(itemKey).get("content").isEmpty()) {
                          StringBuilder description = new StringBuilder();
                          item.getFields()
                              .get(itemKey)
                              .get("content")
                              .forEach(
                                  (content) -> {
                                    int index = 0;
                                    content
                                        .get("content")
                                        .forEach(
                                            (content1) -> {
                                              if (index == 0) {
                                                description.append(content1.get("value").asText());
                                              } else {
                                                description
                                                    .append("<br>")
                                                    .append(content1.get("value").asText());
                                              }
                                            });
                                  });
                          cmsData.put(itemKey, description);
                        } else cmsData.put(itemKey, item.getFields().get(itemKey));
                      });
              data.add(
                  new CMSData(
                      item.getSys().getContentType().getSys().getId(),
                      item.getSys().getLocale(),
                      cmsData));
            });
    return data;
  }
}
