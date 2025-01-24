package com.Dongo.GodLife.ScheduleBundle.Schedule.Service;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SchedulePersistenceAdapter {

    Schedule save(Schedule schedule);

    Optional<Schedule> findById(long ScheduleId);

}
