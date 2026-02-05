package com.example.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceUrlsConfig {

    @Value("${service.auth.url:http://authmicroservice:9020}")
    private String authServiceUrl;

    @Value("${service.database.url:http://dbmicroservice:9009}")
    private String databaseServiceUrl;

    @Value("${service.task.url:http://taskmicroservice:9010}")
    private String taskServiceUrl;

    @Value("${service.competition.url:http://competitionmicroservice:9011}")
    private String competitionServiceUrl;

    @Value("${service.list.url:http://listmicroservice:9014}")
    private String listServiceUrl;

    @Value("${service.user.url:http://usermicroservice:9015}")
    private String userServiceUrl;

    @Value("${service.feedback.url:http://feedbackmicroservice:9016}")
    private String feedbackServiceUrl;

    @Value("${service.reporting.url:http://reportingmicroservice:9017}")
    private String reportingServiceUrl;

    @Value("${service.project.url:http://projectmicroservice:9018}")
    private String projectServiceUrl;

    @Value("${service.product.url:http://productmicroservice:9019}")
    private String productServiceUrl;

    public String getAuthServiceUrl() {
        return authServiceUrl;
    }

    public String getDatabaseServiceUrl() {
        return databaseServiceUrl;
    }

    public String getTaskServiceUrl() {
        return taskServiceUrl;
    }

    public String getCompetitionServiceUrl() {
        return competitionServiceUrl;
    }

    public String getListServiceUrl() {
        return listServiceUrl;
    }

    public String getUserServiceUrl() {
        return userServiceUrl;
    }

    public String getFeedbackServiceUrl() {
        return feedbackServiceUrl;
    }

    public String getReportingServiceUrl() {
        return reportingServiceUrl;
    }

    public String getProjectServiceUrl() {
        return projectServiceUrl;
    }

    public String getProductServiceUrl() {
        return productServiceUrl;
    }
}
