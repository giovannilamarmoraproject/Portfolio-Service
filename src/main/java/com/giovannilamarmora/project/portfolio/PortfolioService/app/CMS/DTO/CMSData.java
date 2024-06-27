package com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.giovannilamarmora.utils.generic.GenericDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
