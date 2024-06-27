package com.giovannilamarmora.project.portfolio.PortfolioService.app.model;

public record PortfolioWork(
    String identifier,
    String title,
    String from,
    String to,
    String role,
    String where,
    String description) {}
