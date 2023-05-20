package com.example.demo.service;

import com.example.demo.dto.Habit;
import com.example.demo.dto.Progress;
import com.example.demo.dto.User;
import com.example.demo.entity.ProgressEntity;
import com.example.demo.mapper.HabitMapper;
import com.example.demo.mapper.ProgressMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.HabitRepository;
import com.example.demo.repository.ProgressRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private UserRepository userRepository;
    private HabitRepository habitRepository;
    @Autowired
    private HabitService habitService;

    public ProgressService(ProgressRepository progressRepository, UserRepository userRepository, HabitRepository habitRepository) {
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.habitRepository = habitRepository;
    }

    public Progress getProgressById(Long id) {
        return ProgressMapper.INSTANCE.progressEntityToProgress(progressRepository.findById(id).get());
    }

    public List<Progress> getAllProgress() {
        List<ProgressEntity> progressEntities = progressRepository.findAll();
        return progressEntities.stream().map(ProgressMapper.INSTANCE::progressEntityToProgress).collect(Collectors.toList());
    }

    public Optional<List<Progress>> getProgressByUserIdAndHabitId(Long userId, Long habitId) {
        Optional<List<ProgressEntity>> progressEntityList = progressRepository.findByUserIdAndHabitId(userId, habitId);
        return progressEntityList.map(progressEntities -> progressEntities.stream().map(ProgressMapper.INSTANCE::progressEntityToProgress).collect(Collectors.toList()));
    }

    public Optional<Progress> getProgressByUserAndHabit(User user, Habit habit) {
        Optional<ProgressEntity> progressEntity = progressRepository.findByUserAndHabit(UserMapper.INSTANCE.userToUserEntity(user), HabitMapper.INSTANCE.habitToHabitEntity(habit));
        return Optional.ofNullable(ProgressMapper.INSTANCE.progressEntityToProgress(progressEntity.orElse(null)));
    }

    public Optional<Progress> getProgressByUserAndHabitAndUpdateDate(User user, Habit habit, LocalDateTime updateDate) {
        Optional<ProgressEntity> progressEntities = progressRepository.findByUserAndHabitAndUpdateDate(UserMapper.INSTANCE.userToUserEntity(user), HabitMapper.INSTANCE.habitToHabitEntity(habit), updateDate);
        return Optional.ofNullable(ProgressMapper.INSTANCE.progressEntityToProgress(progressEntities.get()));
    }

    public Optional<Progress> getProgressByUserAndHabitAndUpdateDateAndIsDone(User user, Habit habit, LocalDateTime updateDate, Boolean isDone) {
        Optional<ProgressEntity> progressEntity = progressRepository.findByUserAndHabitAndUpdateDateAndIsDone(UserMapper.INSTANCE.userToUserEntity(user), HabitMapper.INSTANCE.habitToHabitEntity(habit), updateDate, isDone);
        return Optional.ofNullable(ProgressMapper.INSTANCE.progressEntityToProgress(progressEntity.get()));
    }

    public Optional<List<Progress>> getProgressListByUserAndHabitAndIsDone(User user, Habit habit, Boolean isDone) {
        Optional<List<ProgressEntity>> progressEntity = progressRepository.findByUserAndHabitAndIsDone(UserMapper.INSTANCE.userToUserEntity(user), HabitMapper.INSTANCE.habitToHabitEntity(habit), isDone);
        return progressEntity.map(progressEntities -> progressEntities.stream().map(ProgressMapper.INSTANCE::progressEntityToProgress).collect(Collectors.toList()));
    }

    public Progress addProgress(Long userId, Long habitId) {
        User user = UserMapper.INSTANCE.userEntityToUser(userRepository.findById(userId).get());
        Habit habit = HabitMapper.INSTANCE.habitEntityToHabit(habitRepository.findById(habitId).get());

        Optional<Progress> progress = Optional.empty();
        if(user != null && habit != null) {
            progress = getProgressByUserAndHabit(user, habit);
            if(progress.isEmpty()) { saveProgress(new Progress(LocalDateTime.now(), true, user, habit)); }
            else {
                progress.get().setUpdateDate(LocalDateTime.now());
                progress.get().setIsDone(true);
            }
        }
        habitService.updateHabitCount(user, habit);
        ProgressEntity progressEntity = ProgressMapper.INSTANCE.progressToProgressEntity(progress.get());
        return ProgressMapper.INSTANCE.progressEntityToProgress(progressRepository.save(progressEntity));
    }

    public Progress saveProgress(Progress progress) {
        ProgressEntity progressEntity = ProgressMapper.INSTANCE.progressToProgressEntity(progress);
        return ProgressMapper.INSTANCE.progressEntityToProgress(progressRepository.save(progressEntity));
    }

    public void deleteProgress(Progress progress) {
        progressRepository.delete(ProgressMapper.INSTANCE.progressToProgressEntity(progress));
    }
}

