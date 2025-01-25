package com.Dongo.GodLife.ScheduleBundle.Alarm.Service;


import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;

import java.util.Optional;


public interface AlarmPersistenceAdapter {

    Alarm save(Alarm alarm);

    Optional<Alarm> findById(long alarmId);

    Alarm delete(Alarm alarm) throws NotYourScheduleException;
}
