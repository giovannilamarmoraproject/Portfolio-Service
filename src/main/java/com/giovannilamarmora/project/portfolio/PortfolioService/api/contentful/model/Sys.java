package com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sys {
  private String id;
  private String linkType;
  private String type;
  private ContentType contentType;
  private String locale;
  private String createdAt;
  private String updatedAt;
}
