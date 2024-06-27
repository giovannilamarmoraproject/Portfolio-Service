package com.giovannilamarmora.project.portfolio.PortfolioService.app.model;

import java.util.List;

public record PortfolioData(
    String curriculum_url,
    String profilePhoto_url,
    String biography,
    Integer work_projects,
    Integer personal_projects,
    Integer number_courses,
    List<PortfolioWork> works,
    List<PortfolioCourse> courses,
    List<PortfolioProject> projects) {}
