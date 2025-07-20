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


@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;
    private final UserService userService;
    private final ScheduleService scheduleService;

    @PostMapping("/schedule/{schedule_id}/users/{user_id}")
    public ResponseEntity<Alarm> createAlarm(
            @PathVariable(name = "schedule_id") Long schedule_id,
            @PathVariable(name = "user_id") Long user_id,
            @RequestBody AlarmRequest alarmRequest) {
        User user = userService.CheckUserAndGetUser(user_id);
        Schedule schedule = scheduleService.getScheduleById(schedule_id);
        System.out.println(alarmRequest);
        Alarm alarm = alarmService.createAlarm(alarmRequest, schedule, user);
        return ResponseEntity.ok(alarm);
    }

    @GetMapping("/alarm/{alarm_id}/user/{user_id}")
    public ResponseEntity<Alarm> getAlarm(
            @PathVariable(name = "alarm_id") Long alarm_id,
            @PathVariable(name = "user_id") Long user_id) throws NotYourAlarmException {
        User user = userService.CheckUserAndGetUser(user_id);
        Alarm alarm = alarmService.getAlarmByID(alarm_id);
        if(!alarm.getUser().getId().equals(user.getId())){
            throw new NotYourAlarmException("Access denied: User does not own the alarm");
        }
        return ResponseEntity.ok(alarm);
    }

    @PutMapping("/alarm/{alarm_id}/user/{user_id}")
    public ResponseEntity<Alarm> updateAlarm(
            @PathVariable(name = "alarm_id") Long alarm_id,
            @PathVariable(name = "user_id") Long user_id,
            @RequestBody AlarmRequest alarmRequest) throws NotYourAlarmException {
        User user = userService.CheckUserAndGetUser(user_id);
        Alarm updatedAlarm = alarmService.updateAlarm(alarm_id, alarmRequest, user);
        return ResponseEntity.ok(updatedAlarm);
    }


    @DeleteMapping("/alarm/{alarm_id}/user/{user_id}")
    public ResponseEntity<Void> deleteAlarm(
            @PathVariable(name = "alarm_id") Long alarm_id,
            @PathVariable(name = "user_id") Long user_id) throws NotYourAlarmException, NotYourScheduleException {
        User user = userService.CheckUserAndGetUser(user_id);
        alarmService.deleteAlarm(alarm_id, user);
        return ResponseEntity.ok().build();
    }
}
