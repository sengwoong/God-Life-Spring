package com.Dongo.GodLife.ScheduleBundle.Alarm.Repository;

import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Service.AlarmPersistenceAdapter;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class AlarmAdapterImpl implements AlarmPersistenceAdapter {

    private final AlarmRepository alarmRepository;

    public AlarmAdapterImpl(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Override
    public Alarm save(Alarm alarm) {
        return alarmRepository.save(alarm);
    }

    @Override
    public Optional<Alarm> findById(Long id) {
        return alarmRepository.findById(id);
    }

    @Override
    public Alarm delete(Alarm alarm) {
        alarmRepository.delete(alarm);
        return alarm;
    }
}
