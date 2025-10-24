package com.habittracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CreateHabitRequest {
    
    @NotBlank(message = "Habit name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Daily goal is required")
    @Min(value = 1, message = "Daily goal must be at least 1")
    private Integer dailyGoal;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    // Constructors
    public CreateHabitRequest() {}
    
    public CreateHabitRequest(String name, String description, Integer dailyGoal, Long userId) {
        this.name = name;
        this.description = description;
        this.dailyGoal = dailyGoal;
        this.userId = userId;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getDailyGoal() {
        return dailyGoal;
    }
    
    public void setDailyGoal(Integer dailyGoal) {
        this.dailyGoal = dailyGoal;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
