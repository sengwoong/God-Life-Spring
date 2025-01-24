package com.Dongo.GodLife.ScheduleBundle.Alarm.Service;


import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;

import java.util.Optional;


public interface AlarmPersistenceAdapter {

    Alarm save(Alarm alarm);

    Optional<Alarm> findById(long alarmId);

}
