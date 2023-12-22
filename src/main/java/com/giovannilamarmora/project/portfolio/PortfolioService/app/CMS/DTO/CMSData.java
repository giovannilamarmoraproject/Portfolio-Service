package com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.giovannilamarmora.project.portfolio.PortfolioService.generic.GenericDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CMSData extends GenericDTO {
  @NotNull private String contentType;
  @NotNull private String locale;
  @NotNull private Map<String, Object> data;
}
