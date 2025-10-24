package com.habittracker.service;

import com.habittracker.entity.User;
import com.habittracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get or create a user with the given ID
     */
    public User getOrCreateUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            User newUser = new User(userId);
            return userRepository.save(newUser);
        }
    }
    
    /**
     * Update user's last activity date and calculate streak
     */
    public void updateUserActivity(Long userId) {
        User user = getOrCreateUser(userId);
        LocalDate today = LocalDate.now();
        
        // If this is the first activity or a new day
        if (user.getLastActivityDate() == null) {
            user.setLastActivityDate(today);
            user.setTotalStreak(1);
        } else {
            LocalDate lastActivity = user.getLastActivityDate();
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastActivity, today);
            
            if (daysBetween == 1) {
                // Consecutive day - increase streak
                user.setTotalStreak(user.getTotalStreak() + 1);
                user.setLastActivityDate(today);
            } else if (daysBetween > 1) {
                // Gap of more than 1 day - reset streak
                user.setTotalStreak(1);
                user.setLastActivityDate(today);
            }
            // If daysBetween == 0, it's the same day, don't update
        }
        
        userRepository.save(user);
    }
    
    /**
     * Check and update streak based on daily reset logic
     * This is called during daily reset to handle streak continuation
     */
    public void checkAndUpdateStreakAfterReset(Long userId) {
        User user = getOrCreateUser(userId);
        LocalDate today = LocalDate.now();
        
        if (user.getLastActivityDate() != null) {
            LocalDate lastActivity = user.getLastActivityDate();
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastActivity, today);
            
            if (daysBetween >= 2) {
                // If no activity for 2+ days, reset streak
                user.setTotalStreak(0);
                user.setLastActivityDate(null);
                userRepository.save(user);
            }
            // If daysBetween == 1, keep streak as is (user has 1 day to add habits)
        }
    }
    
    /**
     * Get user's current streak
     */
    public Integer getUserStreak(Long userId) {
        User user = getOrCreateUser(userId);
        return user.getTotalStreak();
    }
}
