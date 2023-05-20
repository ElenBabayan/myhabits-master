package com.example.demo;

import com.example.demo.dto.Habit;
import com.example.demo.dto.Progress;
import com.example.demo.dto.Reminder;
import com.example.demo.dto.User;
import com.example.demo.service.HabitService;
import com.example.demo.service.ProgressService;
import com.example.demo.service.ReminderService;
import com.example.demo.service.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

public class HabitReminderJob implements Job {
    @Autowired
    private UserService userService;
    @Autowired
    private HabitService habitService;
    @Autowired
    private ProgressService progressService;
    @Autowired
    private ReminderService reminderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        habitCompletenessNotification();

        habitsDueWithinHourNotification();
    }

    public void habitCompletenessNotification() {
        // Query habits that are completed
        User user = userService.find();
        List<Habit> habitList = habitService.findByUser();
        habitList.stream().filter(habit -> habit.getDueDate().isAfter(LocalDateTime.now())).forEach(habit -> {

            Optional<List<Progress>> progressList = progressService.getProgressListByUserAndHabitAndIsDone(user, habit, true);
            int totalDays = Period.between(habit.getDueDate().toLocalDate(), habit.getCreatedDate().toLocalDate()).getDays();

            int daysCompleted = 0;
            if(progressList.isPresent()) {
                for(Progress progress : progressList.get()) {
                    daysCompleted += progress.getIsDone() ? 1 : 0;
                }
            }
            if(daysCompleted >= totalDays * 0.85) {
                Reminder reminder = new Reminder("COMPLETED", "Habit: " + habit.getName() + " is completed", ReminderStatus.SUCCESS);
                reminderService.sendReminder(reminder);
            }

            if(daysCompleted <= totalDays * 0.5) {
                Reminder reminder = new Reminder("INCOMPLETE", "Habit: " + habit.getName() + " is not completed", ReminderStatus.LATE);
                reminderService.sendReminder(reminder);
            }
        });
    }

    public void habitsDueWithinHourNotification() {
        // Query habits that are due within 1 hour
        User user = userService.find();
        List<Habit> habitList = habitService.findByUser();
        for(Habit habit : habitList) {
            Optional<List<Progress>> progressList = progressService.getProgressByUserIdAndHabitId(user.getId(), habit.getId());

            progressList.get().stream()
                    .filter(progress -> !progress.getIsDone() && habit.getDueDate().minusHours(1).isBefore(LocalDateTime.now()))
                    .forEach(progress -> {
                        Reminder reminder = new Reminder("WARNING", "1 hour left until the due date of habit: " + habit.getName(), ReminderStatus.WARNING);
                        reminderService.sendReminder(reminder);
                    });
        }
    }
}