package com.example.demo.dto;

import com.example.demo.ReminderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {
    private Long id;
    private String title;
    private String description;
    private Boolean isSeen;
    private ReminderStatus status;
    private User user;
    private Habit habit;
    public Reminder (String title, String description, ReminderStatus status) {
    }
}
