package com.example.demo.controller;

import com.example.demo.dto.Habit;
import com.example.demo.dto.Progress;
import com.example.demo.dto.User;
import com.example.demo.service.HabitService;
import com.example.demo.service.ProgressService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;
    private final HabitService habitService;
    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveProgress(@Validated @RequestBody Progress progress, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Progress savedProgress = progressService.saveProgress(progress);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProgress.getId()).toUri();
        return ResponseEntity.created(location).body(savedProgress);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProgressById(@PathVariable("id") Long id) {
        Optional<Progress> progress = Optional.ofNullable(progressService.getProgressById(id));
        return progress.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/habit/{habitId}")
    public ResponseEntity<?> getProgressByUserAndHabit(
            @PathVariable("userId") Long userId, @PathVariable("habitId") Long habitId) {
        User user = userService.findById(userId);
        Habit habit = habitService.findById(habitId);
        if (user != null && habit != null) {
            Optional<Progress> progress = progressService.getProgressByUserAndHabit(user, habit);
            return progress.map(value -> ResponseEntity.ok().body(value))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProgress() {
        List<Progress> progressList = progressService.getAllProgress();
        if (progressList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(progressList);
        }
    }

    @GetMapping("/user/{userId}/habit/{habitId}/updateDate")
    public ResponseEntity<?> getProgressByUserAndHabitAndUpdateDate(
            @PathVariable("userId") Long userId, @PathVariable("habitId") Long habitId, @RequestParam("updateDate") LocalDateTime updateDate) {
        User user = userService.findById(userId);
        Habit habit = habitService.findById(habitId);
        if (user != null && habit != null) {
            Optional<Progress> progressList = progressService.getProgressByUserAndHabitAndUpdateDate(user, habit, updateDate);
            if (progressList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok().body(progressList);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/{userId}/habit/{habitId}")
    public ResponseEntity<?> addProgress(@PathVariable("userId") Long userId, @PathVariable("habitId") Long habitId) {
        Progress progress = progressService.addProgress(userId, habitId);
        return ResponseEntity.ok().body(progress);
    }

    @DeleteMapping("/progress/{id}")
    public ResponseEntity<?> deleteProgress(@PathVariable("id") Long id) {
        Optional<Progress> progress = Optional.ofNullable(progressService.getProgressById(id));

        if (progress.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        progressService.deleteProgress(progress.get());
        return ResponseEntity.noContent().build();
    }
}
