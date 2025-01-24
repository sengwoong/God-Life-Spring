package com.Dongo.GodLife.ScheduleBundle.Schedule.Repository;



import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.SchedulePersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class ScheduleAdapterImpl implements SchedulePersistenceAdapter {

    private final ScheduleRepository scheduleRepository;

    // scheduleRepository를 생성자 주입
    public ScheduleAdapterImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Optional<Schedule> findById(long id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public Page<Schedule> findByUser(User user, Pageable pageable) {
        return scheduleRepository.findByUser(user, pageable);
    }

}
