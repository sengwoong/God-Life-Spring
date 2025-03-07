package com.Dongo.GodLife.ScheduleBundle.Alarm.Repository;

import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUserId(long userId);
}