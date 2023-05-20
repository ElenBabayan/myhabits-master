package com.example.demo.mapper;

import com.example.demo.dto.Reminder;
import com.example.demo.entity.ReminderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReminderMapper {
    ReminderMapper INSTANCE = Mappers.getMapper( ReminderMapper.class );
    Reminder reminderEntityToReminder(ReminderEntity reminderEntity);
    ReminderEntity reminderToReminderEntity(Reminder reminder);
}
