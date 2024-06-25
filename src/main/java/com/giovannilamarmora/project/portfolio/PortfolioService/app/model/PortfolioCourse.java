package com.giovannilamarmora.project.portfolio.PortfolioService.app.model;

public record PortfolioCourse(
    String identifier,
    String title,
    String from,
    String to,
    String role,
    String where,
    String description) {}
