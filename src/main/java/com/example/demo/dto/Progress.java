package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Progress {
    private Long id;
    private LocalDateTime updateDate;
    private Boolean isDone;
    private User user;
    private Habit habit;
    public Progress(LocalDateTime updateDate, Boolean isDone, User user, Habit habit) {
        this.updateDate = updateDate;
        this.isDone = isDone;
        this.user = user;
        this.habit = habit;
    }
}
