package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "habit")
@Getter
@Setter
public class HabitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_generator")
    @SequenceGenerator(name = "my_generator", sequenceName = "habit_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "current_streak")
    private Integer currentStreak;

    @Column(name = "longest_streak")
    private Integer longestStreak;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    public HabitEntity(String name, String description, LocalDateTime createdDate, LocalDateTime dueDate, Integer totalCount, Integer currentStreak, Integer longestStreak) {
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.totalCount = totalCount;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
    }

    public HabitEntity() {
    }
}
