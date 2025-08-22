package com.Dongo.GodLife.ScheduleBundle.Schedule;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.SchedulePersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SchedulePersistenceAdapterStub implements SchedulePersistenceAdapter {

    private final List<Schedule> scheduleList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Schedule save(Schedule schedule) {
        if (schedule.getScheduleId() == null) {
            schedule.setScheduleId(idGenerator.getAndIncrement());
            scheduleList.add(schedule);
        } else {
            scheduleList.removeIf(existingSchedule -> existingSchedule.getScheduleId().equals(schedule.getScheduleId()));
            scheduleList.add(schedule);
        }
        return schedule;
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return scheduleList.stream()
                .filter(schedule -> schedule.getScheduleId().equals(id))
                .findFirst();
    }

    @Override
    public Page<Schedule> findByUser(User user, Pageable pageable) {
        List<Schedule> userSchedules = scheduleList.stream()
                .filter(schedule -> schedule.getUser().equals(user))
                .collect(Collectors.toList());
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), userSchedules.size());
        
        return new PageImpl<>(
            userSchedules.subList(start, end),
            pageable,
            userSchedules.size()
        );
    }

    @Override
    public Schedule delete(Schedule schedule) throws NotYourScheduleException {
        scheduleList.removeIf(existingSchedule -> existingSchedule.getScheduleId().equals(schedule.getScheduleId()));
        return schedule;
    }
} 