package com.habittracker.dto;

import com.habittracker.entity.Habit;
import java.time.LocalDateTime;

public class HabitResponse {
    
    private Long id;
    private String name;
    private String description;
    private Integer dailyGoal;
    private Integer streakCount;
    private Double progressPercent;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean completedToday;
    private Integer completedCountToday;
    
    // Constructors
    public HabitResponse() {}
    
    public HabitResponse(Habit habit) {
        this.id = habit.getId();
        this.name = habit.getName();
        this.description = habit.getDescription();
        this.dailyGoal = habit.getDailyGoal();
        this.streakCount = habit.getStreakCount();
        this.progressPercent = habit.getProgressPercent();
        this.userId = habit.getUserId();
        this.createdAt = habit.getCreatedAt();
        this.updatedAt = habit.getUpdatedAt();
        this.completedToday = false; // This will be set based on today's log
        this.completedCountToday = 0; // This will be set based on today's log
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Integer getStreakCount() {
        return streakCount;
    }
    
    public void setStreakCount(Integer streakCount) {
        this.streakCount = streakCount;
    }
    
    public Double getProgressPercent() {
        return progressPercent;
    }
    
    public void setProgressPercent(Double progressPercent) {
        this.progressPercent = progressPercent;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getCompletedToday() {
        return completedToday;
    }
    
    public void setCompletedToday(Boolean completedToday) {
        this.completedToday = completedToday;
    }
    
    public Integer getCompletedCountToday() {
        return completedCountToday;
    }
    
    public void setCompletedCountToday(Integer completedCountToday) {
        this.completedCountToday = completedCountToday;
    }
}
