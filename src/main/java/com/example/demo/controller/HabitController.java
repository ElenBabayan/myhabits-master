package com.example.demo.controller;

import com.example.demo.dto.Habit;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.HabitService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService, UserService userService) {
        this.habitService = habitService;
    }

    @PostMapping
    public ResponseEntity<?> createHabit(@RequestBody Habit habit) {
        return ResponseEntity.status(HttpStatus.CREATED).body( habitService.create(habit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHabitById(@PathVariable Long id) {
        Habit optionalHabit = habitService.findById(id);
        if (optionalHabit != null) {
            return ResponseEntity.ok(optionalHabit);
        } else {
            throw new ResourceNotFoundException("Habit not found");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getHabitsByUser() {
        List<Habit> habits = habitService.findByUser();
        if (habits.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(habits);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllHabits() {
        List<Habit> habits = habitService.findAll();
        if (habits.isEmpty()) {
            throw new ResourceNotFoundException("No habits found");
        } else {
            return ResponseEntity.ok(habits);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHabit(@PathVariable Long id, @RequestBody Habit updatedHabit) {
        Habit habit = habitService.updateHabit(id, updatedHabit);
        return ResponseEntity.ok(habit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        Habit optionalHabit = habitService.findById(id);
        if (optionalHabit != null) {
            habitService.delete(optionalHabit);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Habit not found");
        }
    }
}

