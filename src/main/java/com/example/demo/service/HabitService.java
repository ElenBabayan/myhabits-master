package com.example.demo.service;

import com.example.demo.dto.Habit;
import com.example.demo.dto.User;
import com.example.demo.entity.HabitEntity;
import com.example.demo.entity.ProgressEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mapper.HabitMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.HabitRepository;
import com.example.demo.repository.ProgressRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final ProgressRepository progressRepository;

    public HabitService(HabitRepository habitRepository,
                        UserRepository userRepository,
                        ProgressRepository progressRepository) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
        this.progressRepository = progressRepository;
    }

    public Habit findById(Long id) {
        return HabitMapper.INSTANCE.habitEntityToHabit(habitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Habit " + id)));
    }

    public List<Habit> findAll() {
        return habitRepository.findAll().stream()
                .map(HabitMapper.INSTANCE::habitEntityToHabit).collect(Collectors.toList());
    }

    public Habit create(Habit habit) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HabitEntity entity = HabitMapper.INSTANCE.habitToHabitEntity(habit);
        UserEntity user = userRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        entity.setUser(user);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setTotalCount(0);
        entity.setCurrentStreak(0);
        entity.setLongestStreak(0);
        return HabitMapper.INSTANCE.habitEntityToHabit(habitRepository.save(entity));
    }

    public List<Habit> findByUser() {
        UserEntity userEntity = userRepository.findByEmail(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
                .orElseThrow();
        return habitRepository.findByUser(userEntity).stream()
                .map(HabitMapper.INSTANCE::habitEntityToHabit)
                .collect(Collectors.toList());
    }

    public Habit save(Habit habit) {
        HabitEntity habitEntity = HabitMapper.INSTANCE.habitToHabitEntity(habit);

        return HabitMapper.INSTANCE.habitEntityToHabit(habitRepository.save(habitEntity));
    }

    public Habit updateHabit(long id, Habit updatedHabit) {
        HabitEntity habit = habitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habit " + id));

        if (updatedHabit.getName() != null && !updatedHabit.getName().isEmpty()) {
            habit.setName(updatedHabit.getName());
        }
        if (updatedHabit.getDescription() != null) {
            habit.setDescription(updatedHabit.getDescription());
        }
        if(updatedHabit.getDueDate() != null) {
            habit.setDueDate(updatedHabit.getDueDate());
        }

        return HabitMapper.INSTANCE.habitEntityToHabit(habitRepository.save(habit));
    }

    public Habit updateHabitCount(User user, Habit habit) {
        LocalDateTime previousDay = LocalDateTime.now().minus(Period.ofDays(1));
        ProgressEntity lastProgress = progressRepository
                .findByUserAndHabitAndUpdateDate(UserMapper.INSTANCE.userToUserEntity(user), HabitMapper.INSTANCE.habitToHabitEntity(habit), previousDay).orElse(null);
        // first day of habit
        if(lastProgress == null) {
            habit.setCurrentStreak(1);
        } else {
            habit.setCurrentStreak(lastProgress.getIsDone()? habit.getCurrentStreak() + 1 : 0);
        }

        if(habit.getCurrentStreak() > habit.getLongestStreak()) {
            habit.setLongestStreak(habit.getCurrentStreak());
        }
        habit.setTotalCount(habit.getTotalCount() + 1);

        return HabitMapper.INSTANCE.habitEntityToHabit(habitRepository.save(HabitMapper.INSTANCE.habitToHabitEntity(habit)));
    }

    public HashMap<LocalDateTime, Integer> getHabitActivityForCurrentWeek() {
        HashMap<LocalDateTime, Integer> activityMap = new HashMap<>();

        LocalDateTime start = LocalDateTime.now().minus(Period.ofDays(7));
        LocalDateTime end = LocalDateTime.now();

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        List<HabitEntity> habits = habitRepository.findByUser(user);

        while(start.isBefore(end)) {
            int totalCount = 0;
            for(HabitEntity habit : habits) {
               ProgressEntity progress = progressRepository.findByUserAndHabitAndUpdateDate(user, habit, start).get();
               totalCount += progress.getIsDone() ? 1 : 0;
            }
            activityMap.put(start, totalCount);
            start = start.plus(Period.ofDays(1));
        }

        return activityMap;
    }

    public void delete(Habit habit) {
        HabitEntity habitEntity = HabitMapper.INSTANCE.habitToHabitEntity(habit);
        habitRepository.delete(habitEntity);
    }
}
