package com.Dongo.GodLife.ScheduleBundle.Alarm.Controller;


import com.Dongo.GodLife.ScheduleBundle.Alarm.Dto.AlarmRequest;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Exception.NotYourAlarmException;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Service.AlarmService;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.ScheduleService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;
    private final UserService userService;
    private final ScheduleService scheduleService;

    @PostMapping("/schedule/{schedule_id}/users/{user_id}")
    public ResponseEntity<?> createAlarm(
            @PathVariable(name = "schedule_id") long schedule_id,
            @PathVariable(name = "user_id") long user_id,
            @RequestBody AlarmRequest alarmRequest) {
        User user = userService.CheckUserAndGetUser(user_id);
        Schedule schedule = scheduleService.getScheduleById(schedule_id);
        
        // 사용자의 알람 개수 확인
        List<Alarm> userAlarms = alarmService.getAlarmsByUserId(user_id);
        if (userAlarms.size() >= 5) {
            return ResponseEntity.badRequest().body("알람은 최대 5개까지만 생성 가능합니다.");
        }
        
        System.out.println(alarmRequest);
        Alarm alarm = alarmService.createAlarm(alarmRequest, schedule, user);
        return ResponseEntity.ok(alarm);
    }

    @GetMapping("/alarm/{alarm_id}")
    public ResponseEntity<Alarm> getAlarm(
            @PathVariable(name = "alarm_id") long alarm_id) {
        Alarm alarm = alarmService.getAlarmByID(alarm_id);
        return ResponseEntity.ok(alarm);
    }

    @PutMapping("/alarm/{alarm_id}/user/{user_id}")
    public ResponseEntity<Alarm> updateAlarm(
            @PathVariable(name = "alarm_id") long alarm_id,
            @PathVariable(name = "user_id") long user_id,
            @RequestBody AlarmRequest alarmRequest) throws NotYourAlarmException {
        Alarm updatedAlarm = alarmService.updateAlarm(alarm_id, user_id, alarmRequest);
        return ResponseEntity.ok(updatedAlarm);
    }


    @DeleteMapping("/alarm/{alarm_id}/user/{user_id}")
    public ResponseEntity<Void> deleteAlarm(
            @PathVariable(name = "alarm_id") long alarm_id,
            @RequestParam(name = "user_id") long user_id) throws NotYourAlarmException, NotYourScheduleException {
        User user = userService.CheckUserAndGetUser(user_id);
        alarmService.deleteAlarm(user, alarm_id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<List<Alarm>> getUserAlarms(
            @PathVariable(name = "user_id") long user_id) {
        User user = userService.CheckUserAndGetUser(user_id);
        List<Alarm> alarms = alarmService.getAlarmsByUserId(user_id);
        return ResponseEntity.ok(alarms);
    }
}
