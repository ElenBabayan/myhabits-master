package com.example.demo.service;

import com.example.demo.dto.Reminder;
import com.example.demo.entity.HabitEntity;
import com.example.demo.entity.ReminderEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.HabitMapper;
import com.example.demo.mapper.ReminderMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.HabitRepository;
import com.example.demo.repository.ReminderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;

    private UserRepository userRepository;

    private HabitRepository habitRepository;

    public ReminderService(ReminderRepository reminderRepository, UserRepository userRepository, HabitRepository habitRepository) {
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
        this.habitRepository = habitRepository;
    }

    public Reminder updateReminder(Reminder updatedReminder) {
        ReminderEntity existingReminder = reminderRepository.findById(updatedReminder.getId())
                .orElseThrow(() -> new EntityNotFoundException("Reminder not found with ID: " + updatedReminder.getId()));

        // Only update fields that were actually passed in the request
        if (updatedReminder.getTitle() != null) {
            existingReminder.setTitle(updatedReminder.getTitle());
        }
        if (updatedReminder.getDescription() != null) {
            existingReminder.setDescription(updatedReminder.getDescription());
        }
        if (updatedReminder.getStatus() != null) {
            existingReminder.setStatus(updatedReminder.getStatus());
        }
        if(updatedReminder.getIsSeen() != null) {
            existingReminder.setIsSeen(updatedReminder.getIsSeen());
        }

        return ReminderMapper.INSTANCE.reminderEntityToReminder(reminderRepository.save(existingReminder));
    }

    public List<Reminder> getRemindersByUserId(Long userId) {
        return reminderRepository.findByUserId(userId).stream().map(ReminderMapper.INSTANCE::reminderEntityToReminder).toList();
    }

    public List<Reminder> getRemindersByHabitId(Long habitId) {
        return reminderRepository.findByHabitId(habitId).stream().map(ReminderMapper.INSTANCE::reminderEntityToReminder).toList();
    }

    public List<Reminder> getRemindersByUserAndHabit(Long userId, Long habitId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<HabitEntity> habit = habitRepository.findById(habitId);
        if(user.isEmpty() || habit.isEmpty()) {
            throw new EntityNotFoundException("User or Habit not found");
        }
        return reminderRepository.findByUserAndHabit(user.get(), habit.get()).orElseThrow(() -> new EntityNotFoundException("Reminder not found"))
                .stream().map(ReminderMapper.INSTANCE::reminderEntityToReminder).toList();
    }

    public List<Reminder> getRemindersByUserAndHabitAndSeen(Long userId, Long habitId, Boolean seen) {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<HabitEntity> habit = habitRepository.findById(habitId);
        if(user.isEmpty() || habit.isEmpty()) {
            throw new EntityNotFoundException("User or Habit not found");
        }
        return reminderRepository.findByUserAndHabitAndIsSeen(user.get(), habit.get(), seen)
                .stream().map(ReminderMapper.INSTANCE::reminderEntityToReminder).toList();
    }

    public List<Reminder> getRemindersByUserAndHabitAndStatus(Long userId, Long habitId, String status) {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<HabitEntity> habit = habitRepository.findById(habitId);
        if(user.isEmpty() || habit.isEmpty()) {
            throw new EntityNotFoundException("User or Habit not found");
        }
        return reminderRepository.findByUserAndHabitAndStatus(user.get(), habit.get(), status)
                .stream().map(ReminderMapper.INSTANCE::reminderEntityToReminder).toList();
    }

    public Reminder createReminder(Reminder reminder, Long habitId) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        Optional<HabitEntity> habit = habitRepository.findById(habitId);
        if(user.isEmpty() || habit.isEmpty()) {
            throw new EntityNotFoundException("User or Habit not found");
        }
        reminder.setUser(UserMapper.INSTANCE.userEntityToUser(user.get()));
        reminder.setHabit(HabitMapper.INSTANCE.habitEntityToHabit(habit.get()));
        return ReminderMapper.INSTANCE.reminderEntityToReminder(reminderRepository.save(ReminderMapper.INSTANCE.reminderToReminderEntity(reminder)));
    }

    public void deleteReminder(Long reminderId) {
        reminderRepository.deleteById(reminderId);
    }

    public void deleteAllReminders() {
        reminderRepository.deleteAll();
    }

    public Optional<Reminder> getReminderById(Long id) {
        return Optional.ofNullable(ReminderMapper.INSTANCE.reminderEntityToReminder(reminderRepository.findById(id).get()));
    }

    public void sendReminder(Reminder reminder) {
        // TODO: Send reminder to front
    }
}
