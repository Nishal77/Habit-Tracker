package com.habittracker.repository;

import com.habittracker.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    
    List<Habit> findByUserId(Long userId);
    
    @Query("SELECT h FROM Habit h WHERE h.userId = :userId ORDER BY h.createdAt DESC")
    List<Habit> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(h) FROM Habit h WHERE h.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM Habit h WHERE h.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
