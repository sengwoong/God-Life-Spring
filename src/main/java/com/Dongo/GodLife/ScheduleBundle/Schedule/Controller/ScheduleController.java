package com.Dongo.GodLife.ScheduleBundle.Schedule.Controller;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Dto.ScheduleRequest;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.ScheduleService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserService userService;

    @PostMapping("/user/{user_id}")
    public ResponseEntity<Schedule> createSchedule(
            @PathVariable(name = "user_id") Long user_id,
            @RequestBody @Valid ScheduleRequest request) {
        User user = userService.CheckUserAndGetUser(user_id);
        return ResponseEntity.ok(scheduleService.createSchedule(request, user));
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<java.util.List<Schedule>> getScheduleByVocaId(
            @PathVariable(name = "user_id") Long user_id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) throws NotYourScheduleException {
        Pageable pageable = PageRequest.of(page, size);
        User user = userService.CheckUserAndGetUser(user_id);
        Page<Schedule> schedules = scheduleService.getAllschedulesByUserId(user, pageable);
        return ResponseEntity.ok(schedules.getContent());
    }

    @GetMapping("/schedule/{schedule_id}/user/{user_id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable(name = "schedule_id") Long schedule_id, @PathVariable(name = "user_id") Long user_id) throws NotYourScheduleException {
        User user = userService.CheckUserAndGetUser(user_id);
        
        Schedule schedule = scheduleService.getScheduleById(schedule_id);
        if(!schedule.getUser().getId().equals(user_id)){
            throw new NotYourScheduleException("Access denied: User does not own the schedule");
        }
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/schedule/{schedule_id}/user/{user_id}")
    public ResponseEntity<Schedule> updateSchedule(
            @PathVariable(name = "schedule_id") Long schedule_id,
            @PathVariable(name = "user_id") Long user_id,
            @RequestBody @Valid ScheduleRequest scheduleRequest) throws NotYourScheduleException {
        User user = userService.CheckUserAndGetUser(user_id);
        Schedule updatedSchedule = scheduleService.updateSchedule(schedule_id, scheduleRequest, user);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/schedule/{schedule_id}/user/{user_id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable(name = "user_id") Long user_id,
            @PathVariable(name = "schedule_id") Long schedule_id) throws NotYourScheduleException {
        User user = userService.CheckUserAndGetUser(user_id);
        scheduleService.deleteSchedule(schedule_id, user);
        return ResponseEntity.noContent().build();
    }
}
