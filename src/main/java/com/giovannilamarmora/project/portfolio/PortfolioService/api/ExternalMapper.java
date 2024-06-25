package com.giovannilamarmora.project.portfolio.PortfolioService.api;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.strapi.dto.StrapiPortfolio;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioCourse;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioData;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioProject;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioWork;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExternalMapper {

  @LogInterceptor(type = LogTimeTracker.ActionType.MAPPER)
  public static PortfolioData mapPortfolioData(
      StrapiPortfolio portfolioData,
      List<StrapiPortfolio> portfolioWorks,
      List<StrapiPortfolio> portfolioCourses,
      List<StrapiPortfolio> portfolioProjects) {
    List<PortfolioWork> portfolioWorkList =
        portfolioWorks.stream().map(ExternalMapper::mapStrapiWorkToPortfolioWork).toList();
    List<PortfolioCourse> portfolioCourseList =
        portfolioWorks.stream().map(ExternalMapper::mapStrapiCourseToPortfolioCourse).toList();
    List<PortfolioProject> portfolioProjectList =
        portfolioWorks.stream().map(ExternalMapper::mapStrapiProjectToPortfolioProject).toList();
    return new PortfolioData(
        portfolioData.getCurriculum_url(),
        portfolioData.getProfilePhoto_url(),
        portfolioData.getBiography(),
        portfolioData.getWork_projects(),
        portfolioData.getPersonal_projects(),
        portfolioData.getCourses(),
        portfolioWorkList,
        portfolioCourseList,
        portfolioProjectList);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.MAPPER)
  public static PortfolioWork mapStrapiWorkToPortfolioWork(StrapiPortfolio portfolioWorks) {
    return new PortfolioWork(
        portfolioWorks.getIdentifier(),
        portfolioWorks.getTitle(),
        portfolioWorks.getFrom(),
        portfolioWorks.getTo(),
        portfolioWorks.getRole(),
        portfolioWorks.getWhere(),
        portfolioWorks.getDescription());
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.MAPPER)
  public static PortfolioCourse mapStrapiCourseToPortfolioCourse(StrapiPortfolio portfolioCourse) {
    return new PortfolioCourse(
        portfolioCourse.getIdentifier(),
        portfolioCourse.getTitle(),
        portfolioCourse.getFrom(),
        portfolioCourse.getTo(),
        portfolioCourse.getRole(),
        portfolioCourse.getWhere(),
        portfolioCourse.getDescription());
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.MAPPER)
  public static PortfolioProject mapStrapiProjectToPortfolioProject(
      StrapiPortfolio portfolioProject) {
    return new PortfolioProject(
        portfolioProject.getTitle(),
        portfolioProject.getImage(),
        portfolioProject.getDescription(),
        portfolioProject.getButton_text(),
        portfolioProject.getButton_link());
  }
}
