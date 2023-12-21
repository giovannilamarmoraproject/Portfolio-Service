package com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.giovannilamarmora.project.portfolio.PortfolioService.generic.GenericEntity;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "CMS_DATA")
public class CMSEntity extends GenericEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "LOCALE", nullable = false)
  private String locale;

  @Column(name = "CONTENT_TYPE", nullable = false)
  private String contentType;

  @Lob
  @Column(name = "DATA", nullable = false)
  private String data;
}
