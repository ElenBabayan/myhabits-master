package com.example.demo.repository;

import com.example.demo.entity.HabitEntity;
import com.example.demo.entity.ProgressEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {
    List<ProgressEntity> findByUser(UserEntity user);

    List<ProgressEntity> findByHabit(HabitEntity habit);

    Optional<ProgressEntity> findByUserAndHabit(UserEntity user, HabitEntity habit);

    Optional<List<ProgressEntity>> findByUserIdAndHabitId(Long userId, Long habitId);

    Optional<ProgressEntity> findByUserAndHabitAndUpdateDate(UserEntity user, HabitEntity habit, LocalDateTime updateDate);

    Optional<List<ProgressEntity>> findByUserAndHabitAndIsDone(UserEntity user, HabitEntity habit, Boolean isDone);

    Optional<ProgressEntity> findByUserAndHabitAndUpdateDateAndIsDone(UserEntity user, HabitEntity habit, LocalDateTime updateDate, Boolean isDone);
}
