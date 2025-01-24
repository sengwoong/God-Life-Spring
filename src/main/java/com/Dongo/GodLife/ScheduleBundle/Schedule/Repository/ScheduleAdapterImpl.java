package com.Dongo.GodLife.ScheduleBundle.Schedule.Repository;



import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.SchedulePersistenceAdapter;
import org.springframework.stereotype.Component;


@Component
public class ScheduleAdapterImpl implements SchedulePersistenceAdapter {

    private final ScheduleRepository scheduleRepository;

    // scheduleRepository를 생성자 주입
    public ScheduleAdapterImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public Schedule save(Schedule schedule) {
        // JpaRepository의 save 메서드 사용
        return scheduleRepository.save(schedule);
    }


}
