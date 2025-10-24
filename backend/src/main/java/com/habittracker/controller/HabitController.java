package com.habittracker.controller;

import com.habittracker.dto.CreateHabitRequest;
import com.habittracker.dto.HabitResponse;
import com.habittracker.entity.Habit;
import com.habittracker.entity.HabitLog;
import com.habittracker.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class HabitController {
    
    @Autowired
    private HabitService habitService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<List<HabitResponse>> getHabitsByUserId(@PathVariable Long userId) {
        // Update all habits to ensure fresh streak and progress data
        habitService.updateAllHabitsForUser(userId);
        
        List<Habit> habits = habitService.getAllHabitsByUserId(userId);
        List<HabitResponse> habitResponses = habits.stream()
                .map(habit -> {
                    HabitResponse response = new HabitResponse(habit);
                    // Check if completed today
                    Optional<HabitLog> todayLog = habitService.getTodayLog(habit.getId());
                    if (todayLog.isPresent()) {
                        response.setCompletedToday(todayLog.get().getIsCompleted());
                        response.setCompletedCountToday(todayLog.get().getCompletedCount());
                    } else {
                        response.setCompletedToday(false);
                        response.setCompletedCountToday(0);
                    }
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(habitResponses);
    }
    
    @GetMapping("/habit/{id}")
    public ResponseEntity<Habit> getHabitById(@PathVariable Long id) {
        Optional<Habit> habit = habitService.getHabitById(id);
        if (habit.isPresent()) {
            return ResponseEntity.ok(habit.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<HabitResponse> createHabit(@Valid @RequestBody CreateHabitRequest request) {
        Habit habit = new Habit(request.getName(), request.getDescription(), request.getDailyGoal(), request.getUserId());
        Habit createdHabit = habitService.createHabit(habit);
        HabitResponse response = new HabitResponse(createdHabit);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Habit> updateHabit(@PathVariable Long id, @Valid @RequestBody Habit habitDetails) {
        try {
            Habit updatedHabit = habitService.updateHabit(id, habitDetails);
            return ResponseEntity.ok(updatedHabit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        try {
            habitService.deleteHabit(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/log")
    public ResponseEntity<HabitLog> markHabitDone(@PathVariable Long id) {
        try {
            HabitLog log = habitService.markHabitDone(id, LocalDate.now());
            habitService.updateHabitProgress(id);
            return ResponseEntity.ok(log);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/log/{completedCount}")
    public ResponseEntity<HabitLog> markHabitPartialDone(@PathVariable Long id, @PathVariable int completedCount) {
        try {
            HabitLog log = habitService.markHabitPartialDone(id, completedCount);
            habitService.updateHabitProgress(id);
            return ResponseEntity.ok(log);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/progress")
    public ResponseEntity<Habit> getHabitProgress(@PathVariable Long id) {
        Optional<Habit> habit = habitService.getHabitById(id);
        if (habit.isPresent()) {
            habitService.updateHabitProgress(id);
            return ResponseEntity.ok(habit.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/logs")
    public ResponseEntity<List<HabitLog>> getHabitLogs(@PathVariable Long id) {
        List<HabitLog> logs = habitService.getHabitLogs(id);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/{id}/today")
    public ResponseEntity<HabitLog> getTodayLog(@PathVariable Long id) {
        Optional<HabitLog> log = habitService.getTodayLog(id);
        if (log.isPresent()) {
            return ResponseEntity.ok(log.get());
        }
        return ResponseEntity.ok(null);
    }
    
    @PostMapping("/{userId}/daily-reset")
    public ResponseEntity<String> dailyReset(@PathVariable Long userId) {
        try {
            habitService.dailyResetForUser(userId);
            return ResponseEntity.ok("Daily reset completed successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to perform daily reset: " + e.getMessage());
        }
    }
    
    @GetMapping("/{userId}/streak")
    public ResponseEntity<Integer> getUserStreak(@PathVariable Long userId) {
        try {
            Integer streak = habitService.getUserStreak(userId);
            return ResponseEntity.ok(streak);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(0);
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint working");
    }
}
