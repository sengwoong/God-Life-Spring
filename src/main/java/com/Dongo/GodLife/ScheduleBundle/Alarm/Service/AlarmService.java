package com.Dongo.GodLife.ScheduleBundle.Alarm.Service;


import com.Dongo.GodLife.ScheduleBundle.Alarm.Exception.NotYourAlarmException;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Dto.AlarmRequest;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import util.Validator;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmPersistenceAdapter alarmRepository;

    public Alarm createAlarm(AlarmRequest alarmRequest, Schedule schedule, User user) {
        Alarm alarm = new Alarm();
        alarm.setUser(user);
        alarm.setAlarmTitle(alarmRequest.getAlarmTitle());
        alarm.setAlarmcontent(alarmRequest.getAlarmContent());
        alarm.setSchedule(schedule);
        return alarmRepository.save(alarm);
    }

    public Alarm getAlarmByID(Long alarmId ) {
        return alarmRepository.findById(alarmId)
                .orElseThrow(() -> new NoSuchElementException("Alarm not found with ID: " + alarmId));
    }

    public Alarm updateAlarm(Long alarmId, AlarmRequest alarmRequest, User user) throws NotYourAlarmException {

        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new EntityNotFoundException("Alarm not found with ID: " + alarmId));

        Validator.validateNotEmpty(alarmRequest.getAlarmTitle(), "Alarm title cannot be empty");
        Validator.validateNotEmpty(alarmRequest.getAlarmContent(), "Alarm content cannot be empty");
        if (!alarm.getUser().getId().equals(user.getId())) {
            throw new NotYourAlarmException("Access denied: User does not own the alarm");
        }
        alarm.setAlarmTitle(alarmRequest.getAlarmTitle());
        alarm.setAlarmcontent(alarmRequest.getAlarmContent());

        return alarmRepository.save(alarm);
    }

    public void deleteAlarm(Long alarmId, User user) throws NotYourAlarmException, NotYourScheduleException {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new IllegalArgumentException("Alarm not found with id: " + alarmId));

        if (!alarm.getUser().getId().equals(user.getId()    )) {
            throw new NotYourAlarmException("Access denied: User does not own the alarm");
        }

        alarmRepository.delete(alarm);
    }
}