package com.example.demo.repository;

import com.example.demo.entity.HabitEntity;
import com.example.demo.entity.ReminderEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {

    List<ReminderEntity> findByHabitId(Long habitId);

    List<ReminderEntity> findByUserId(Long userId);

    Optional<List<ReminderEntity>> findByUserAndHabit(UserEntity user, HabitEntity habit);

    List<ReminderEntity> findByUser(UserEntity user);

    List<ReminderEntity> findByHabit(HabitEntity habit);

    List<ReminderEntity> findByUserAndHabitAndIsSeen(UserEntity user, HabitEntity habit, Boolean seen);

    List<ReminderEntity> findByUserAndHabitAndStatus(UserEntity user, HabitEntity habit, String status);
}
