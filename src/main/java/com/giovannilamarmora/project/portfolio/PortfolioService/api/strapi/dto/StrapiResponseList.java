package com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StrapiResponseList {
  private List<StrapiData> data;

  @Builder
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class StrapiData {
    private String id;
    private StrapiPortfolio attributes;
  }

  @Builder
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Meta {
    private Pagination pagination;
  }

  @Builder
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Pagination {
    private Integer page;
    private Integer pageSize;
    private Integer pageCount;
    private Integer total;
  }
}
