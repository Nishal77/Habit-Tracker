package com.habittracker.repository;

import com.habittracker.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
    
    List<HabitLog> findByHabitId(Long habitId);
    
    Optional<HabitLog> findByHabitIdAndDate(Long habitId, LocalDate date);
    
    @Query("SELECT hl FROM HabitLog hl WHERE hl.habitId = :habitId AND hl.date >= :startDate ORDER BY hl.date DESC")
    List<HabitLog> findByHabitIdAndDateAfter(@Param("habitId") Long habitId, @Param("startDate") LocalDate startDate);
    
    @Query("SELECT COUNT(hl) FROM HabitLog hl WHERE hl.habitId = :habitId AND hl.isCompleted = true")
    Long countCompletedByHabitId(@Param("habitId") Long habitId);
    
    @Query("SELECT COUNT(hl) FROM HabitLog hl WHERE hl.habitId = :habitId AND hl.date >= :startDate AND hl.isCompleted = true")
    Long countCompletedByHabitIdAndDateAfter(@Param("habitId") Long habitId, @Param("startDate") LocalDate startDate);
    
    @Query("SELECT hl FROM HabitLog hl WHERE hl.habitId = :habitId AND hl.isCompleted = true ORDER BY hl.date DESC")
    List<HabitLog> findCompletedByHabitIdOrderByDateDesc(@Param("habitId") Long habitId);
}
