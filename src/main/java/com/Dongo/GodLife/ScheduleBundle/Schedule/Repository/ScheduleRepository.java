package com.Dongo.GodLife.ScheduleBundle.Schedule.Repository;


import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findByUser(User user, Pageable pageable);
}