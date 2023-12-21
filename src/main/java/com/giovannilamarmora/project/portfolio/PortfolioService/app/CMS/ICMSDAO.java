package com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS;

import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.entity.CMSEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICMSDAO extends JpaRepository<CMSEntity, Long> {

    List<CMSEntity> findAllByLocale(String locale);
}
