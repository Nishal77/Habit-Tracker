package com.habittracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "habits")
public class Habit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Habit name is required")
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotNull(message = "Daily goal is required")
    @Min(value = 1, message = "Daily goal must be at least 1")
    @Column(name = "daily_goal", nullable = false)
    private Integer dailyGoal;
    
    @Column(name = "streak_count")
    private Integer streakCount = 0;
    
    @Column(name = "progress_percent")
    private Double progressPercent = 0.0;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HabitLog> habitLogs;
    
    // Constructors
    public Habit() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Habit(String name, String description, Integer dailyGoal, Long userId) {
        this();
        this.name = name;
        this.description = description;
        this.dailyGoal = dailyGoal;
        this.userId = userId;
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
    
    public List<HabitLog> getHabitLogs() {
        return habitLogs;
    }
    
    public void setHabitLogs(List<HabitLog> habitLogs) {
        this.habitLogs = habitLogs;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
