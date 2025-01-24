package com.Dongo.GodLife.ScheduleBundle.Alarm.Service;


import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Dto.AlarmRequest;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Alarm getAlarmByID(long alarmId ) {
        return alarmRepository.findById(alarmId)
                .orElseThrow(() -> new NoSuchElementException("Alarm not found with ID: " + alarmId));
    }
}