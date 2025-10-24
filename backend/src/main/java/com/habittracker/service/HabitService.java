package com.habittracker.service;

import com.habittracker.entity.Habit;
import com.habittracker.entity.HabitLog;
import com.habittracker.repository.HabitRepository;
import com.habittracker.repository.HabitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HabitService {
    
    @Autowired
    private HabitRepository habitRepository;
    
    @Autowired
    private HabitLogRepository habitLogRepository;
    
    @Autowired
    private UserService userService;
    
    public List<Habit> getAllHabitsByUserId(Long userId) {
        return habitRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public Optional<Habit> getHabitById(Long id) {
        return habitRepository.findById(id);
    }
    
    public Habit createHabit(Habit habit) {
        Habit savedHabit = habitRepository.save(habit);
        // Update user activity when creating a habit
        userService.updateUserActivity(habit.getUserId());
        return savedHabit;
    }
    
    public Habit updateHabit(Long id, Habit habitDetails) {
        Optional<Habit> optionalHabit = habitRepository.findById(id);
        if (optionalHabit.isPresent()) {
            Habit habit = optionalHabit.get();
            habit.setName(habitDetails.getName());
            habit.setDescription(habitDetails.getDescription());
            habit.setDailyGoal(habitDetails.getDailyGoal());
            return habitRepository.save(habit);
        }
        throw new RuntimeException("Habit not found with id: " + id);
    }
    
    public void deleteHabit(Long id) {
        habitRepository.deleteById(id);
    }
    
    public HabitLog markHabitDone(Long habitId, LocalDate date) {
        Optional<Habit> optionalHabit = habitRepository.findById(habitId);
        if (!optionalHabit.isPresent()) {
            throw new RuntimeException("Habit not found with id: " + habitId);
        }
        
        Habit habit = optionalHabit.get();
        Optional<HabitLog> existingLog = habitLogRepository.findByHabitIdAndDate(habitId, date);
        
        if (existingLog.isPresent()) {
            HabitLog log = existingLog.get();
            int newCount = log.getCompletedCount() + 1;
            log.setCompletedCount(newCount);
            
            // Check if we've reached the daily goal
            if (newCount >= habit.getDailyGoal()) {
                log.setIsCompleted(true);
            }
            HabitLog savedLog = habitLogRepository.save(log);
            // Update user activity when marking habit as done
            userService.updateUserActivity(habit.getUserId());
            return savedLog;
        } else {
            HabitLog newLog = new HabitLog(habitId, date, 1);
            // Check if this single completion meets the daily goal
            if (habit.getDailyGoal() == 1) {
                newLog.setIsCompleted(true);
            }
            HabitLog savedLog = habitLogRepository.save(newLog);
            // Update user activity when marking habit as done
            userService.updateUserActivity(habit.getUserId());
            return savedLog;
        }
    }
    
    public HabitLog markHabitPartialDone(Long habitId, int completedCount) {
        Optional<Habit> optionalHabit = habitRepository.findById(habitId);
        if (!optionalHabit.isPresent()) {
            throw new RuntimeException("Habit not found with id: " + habitId);
        }
        
        Habit habit = optionalHabit.get();
        LocalDate today = LocalDate.now();
        Optional<HabitLog> existingLog = habitLogRepository.findByHabitIdAndDate(habitId, today);
        
        if (existingLog.isPresent()) {
            HabitLog log = existingLog.get();
            log.setCompletedCount(completedCount);
            log.setIsCompleted(completedCount >= habit.getDailyGoal());
            HabitLog savedLog = habitLogRepository.save(log);
            // Update user activity when marking habit as partially done
            userService.updateUserActivity(habit.getUserId());
            return savedLog;
        } else {
            HabitLog newLog = new HabitLog(habitId, today, completedCount);
            newLog.setIsCompleted(completedCount >= habit.getDailyGoal());
            HabitLog savedLog = habitLogRepository.save(newLog);
            // Update user activity when marking habit as partially done
            userService.updateUserActivity(habit.getUserId());
            return savedLog;
        }
    }
    
    public void updateHabitProgress(Long habitId) {
        Optional<Habit> optionalHabit = habitRepository.findById(habitId);
        if (!optionalHabit.isPresent()) {
            return;
        }
        
        Habit habit = optionalHabit.get();
        
        // Calculate streak count
        int streakCount = calculateStreakCount(habitId);
        habit.setStreakCount(streakCount);
        
        // Calculate progress percentage (based on last 30 days)
        double progressPercent = calculateProgressPercentage(habitId);
        habit.setProgressPercent(progressPercent);
        
        habitRepository.save(habit);
    }
    
    private int calculateStreakCount(Long habitId) {
        List<HabitLog> completedLogs = habitLogRepository.findCompletedByHabitIdOrderByDateDesc(habitId);
        
        if (completedLogs.isEmpty()) {
            return 0;
        }
        
        int streak = 0;
        LocalDate currentDate = LocalDate.now();
        
        // Check if today is completed, if so start counting from today
        boolean todayCompleted = false;
        for (HabitLog log : completedLogs) {
            if (log.getDate().equals(currentDate) && log.getIsCompleted()) {
                todayCompleted = true;
                break;
            }
        }
        
        // If today is completed, start counting from today
        // If not, start counting from yesterday
        LocalDate startDate = todayCompleted ? currentDate : currentDate.minusDays(1);
        
        for (HabitLog log : completedLogs) {
            if (log.getDate().equals(startDate.minusDays(streak))) {
                streak++;
            } else {
                break;
            }
        }
        
        return streak;
    }
    
    private double calculateProgressPercentage(Long habitId) {
        // Check today's progress
        Optional<HabitLog> todayLog = habitLogRepository.findByHabitIdAndDate(habitId, LocalDate.now());
        if (todayLog.isPresent()) {
            HabitLog log = todayLog.get();
            Optional<Habit> habit = habitRepository.findById(habitId);
            if (habit.isPresent()) {
                // Calculate today's progress percentage
                double todayProgress = (log.getCompletedCount().doubleValue() / habit.get().getDailyGoal()) * 100.0;
                return Math.round(todayProgress * 100.0) / 100.0; // Round to 2 decimal places
            }
        }
        
        // If no log for today, return 0%
        return 0.0;
    }
    
    public List<HabitLog> getHabitLogs(Long habitId) {
        return habitLogRepository.findByHabitId(habitId);
    }
    
    public Optional<HabitLog> getTodayLog(Long habitId) {
        return habitLogRepository.findByHabitIdAndDate(habitId, LocalDate.now());
    }
    
    /**
     * Updates all habits for a user - recalculates streaks and progress
     * This should be called when fetching habits to ensure fresh data
     */
    public void updateAllHabitsForUser(Long userId) {
        List<Habit> habits = getAllHabitsByUserId(userId);
        for (Habit habit : habits) {
            updateHabitProgress(habit.getId());
        }
    }
    
    /**
     * Checks if it's a new day and resets daily progress if needed
     * This method can be called periodically to handle day transitions
     */
    public void checkAndHandleDayTransition() {
        // This method can be extended in the future to handle
        // automatic day transitions, but for now we rely on
        // the frontend to refresh data when needed
    }
    
    /**
     * Performs a complete daily reset for a user - removes all habits and recreates them fresh
     * This simulates the cards being completely removed and recreated every 24 hours
     */
    public void dailyResetForUser(Long userId) {
        // Get all habits for the user before deletion
        List<Habit> existingHabits = habitRepository.findByUserIdOrderByCreatedAtDesc(userId);
        
        // Store habit data for recreation
        List<HabitData> habitDataList = existingHabits.stream()
            .map(habit -> new HabitData(habit.getName(), habit.getDescription(), habit.getDailyGoal()))
            .collect(Collectors.toList());
        
        // Delete all habits for the user (this will cascade delete all habit logs)
        habitRepository.deleteByUserId(userId);
        
        // Recreate all habits with fresh data
        for (HabitData habitData : habitDataList) {
            Habit newHabit = new Habit(habitData.name, habitData.description, habitData.dailyGoal, userId);
            habitRepository.save(newHabit);
        }
    }
    
    /**
     * Get user's total streak from UserService
     */
    public Integer getUserStreak(Long userId) {
        return userService.getUserStreak(userId);
    }
    
    /**
     * Check and update user streak after daily reset
     */
    public void checkUserStreakAfterReset(Long userId) {
        userService.checkAndUpdateStreakAfterReset(userId);
    }
    
    /**
     * Helper class to store habit data during reset
     */
    private static class HabitData {
        private final String name;
        private final String description;
        private final int dailyGoal;
        
        public HabitData(String name, String description, int dailyGoal) {
            this.name = name;
            this.description = description;
            this.dailyGoal = dailyGoal;
        }
    }
}
