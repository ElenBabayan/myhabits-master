package com.example.demo.controller;

import com.example.demo.dto.Reminder;
import com.example.demo.service.ReminderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reminder")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping("/create/habit/{habitId}")
    public ResponseEntity<?> createReminder(@Validated @RequestBody Reminder reminder, BindingResult bindingResult, @PathVariable Long habitId) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid reminder data");
        }
        Reminder createdReminder = reminderService.createReminder(reminder, habitId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReminder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReminderById(@PathVariable Long id) {
        Optional<Reminder> reminder = reminderService.getReminderById(id);
        if (reminder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reminder);
    }

    @GetMapping("/user/{userId}")
    public List<?> getRemindersByUserId(@PathVariable Long userId) {
        return reminderService.getRemindersByUserId(userId);
    }

    @GetMapping("/habit/{habitId}")
    public List<?> getRemindersByHabitId(@PathVariable Long habitId) {
        return reminderService.getRemindersByHabitId(habitId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReminder(@PathVariable Long id, @Validated @RequestBody Reminder updatedReminder, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid reminder data");
        }
        updatedReminder.setId(id);
        Reminder savedReminder = reminderService.updateReminder(updatedReminder);
        return ResponseEntity.ok().body(savedReminder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllReminders() {
        reminderService.deleteAllReminders();
        return ResponseEntity.noContent().build();
    }
}
