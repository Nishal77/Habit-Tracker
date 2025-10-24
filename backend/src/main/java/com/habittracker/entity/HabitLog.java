package com.habittracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "habit_logs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"habit_id", "date"})
})
public class HabitLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Habit ID is required")
    @Column(name = "habit_id", nullable = false)
    private Long habitId;
    
    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(name = "completed_count")
    private Integer completedCount = 0;
    
    @Column(name = "is_completed")
    private Boolean isCompleted = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", insertable = false, updatable = false)
    @JsonIgnore
    private Habit habit;
    
    // Constructors
    public HabitLog() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public HabitLog(Long habitId, LocalDate date) {
        this();
        this.habitId = habitId;
        this.date = date;
    }
    
    public HabitLog(Long habitId, LocalDate date, Integer completedCount) {
        this(habitId, date);
        this.completedCount = completedCount;
        this.isCompleted = completedCount > 0;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getHabitId() {
        return habitId;
    }
    
    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Integer getCompletedCount() {
        return completedCount;
    }
    
    public void setCompletedCount(Integer completedCount) {
        this.completedCount = completedCount;
        this.isCompleted = completedCount > 0;
    }
    
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
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
    
    public Habit getHabit() {
        return habit;
    }
    
    public void setHabit(Habit habit) {
        this.habit = habit;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
