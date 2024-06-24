package com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StrapiPortfolio extends StrapiGeneric {

  private Long id;
  private String identifier;
  private String title;
  private String image;
  private String from;
  private String to;
  private String role;
  private String where;
  private String description;
  private String button_text;
  private String button_link;
  private String curriculum_url;
  private String profilePhoto_url;
  private String biography;
  private Integer work_projects;
  private Integer personal_projects;
  private Integer courses;
}
