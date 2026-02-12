package com.example.database_microservice.model;

public enum CompetitionStatus {
    PENDING,    // Competition created but not started
    ACTIVE,     // Competition is ongoing
    COMPLETED,  // Competition finished normally
    CANCELLED   // Competition was cancelled
}
