package com.Dongo.GodLife.ScheduleBundle.Alarm;

import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Service.AlarmPersistenceAdapter;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class AlarmPersistenceAdapterStub implements AlarmPersistenceAdapter {

    private final List<Alarm> alarmList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Alarm save(Alarm alarm) {
        if (alarm.getAlarmId() == null) {
            alarm.setAlarmId(idGenerator.getAndIncrement());
            alarmList.add(alarm);
        } else {
            alarmList.removeIf(existingAlarm -> existingAlarm.getAlarmId().equals(alarm.getAlarmId()));
            alarmList.add(alarm);
        }
        return alarm;
    }

    @Override
    public Optional<Alarm> findById(long id) {
        return alarmList.stream()
                .filter(alarm -> alarm.getAlarmId() == id)
                .findFirst();
    }

    @Override
    public Alarm delete(Alarm alarm) throws NotYourScheduleException {
        alarmList.removeIf(existingAlarm -> existingAlarm.getAlarmId().equals(alarm.getAlarmId()));
        return alarm;
    }
} 