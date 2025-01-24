package com.Dongo.GodLife.ScheduleBundle.Schedule.Service;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SchedulePersistenceAdapter {

    Schedule save(Schedule schedule);

    Optional<Schedule> findById(long ScheduleId);

    Page<Schedule> findByUser(User user, Pageable pageable) throws NotYourScheduleException;

    Schedule delete(Schedule schedule) throws NotYourScheduleException;
}
