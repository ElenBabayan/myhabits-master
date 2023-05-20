package com.example.demo.mapper;

import com.example.demo.dto.Habit;
import com.example.demo.entity.HabitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HabitMapper {
    HabitMapper INSTANCE = Mappers.getMapper( HabitMapper.class );
    Habit habitEntityToHabit(HabitEntity habitEntity);
    HabitEntity habitToHabitEntity(Habit habit);
}
