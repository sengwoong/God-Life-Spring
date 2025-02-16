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

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserService userService;

    @PostMapping("/user/{user_id}")
    public ResponseEntity<Schedule> createSchedule(@PathVariable long user_id,
                                                   @RequestBody ScheduleRequest request) {
        User user = userService.CheckUserAndGetUser(user_id);
        return ResponseEntity.ok(scheduleService.createSchedule(request, user));
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<Schedule>> getScheduleByVocaId(
            @PathVariable long user_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws NotYourScheduleException {
            Pageable pageable = PageRequest.of(page, size);
            User user = userService.CheckUserAndGetUser(user_id);
            Page<Schedule> schedules = scheduleService.getAllschedulesByUserId(user, pageable);
            return ResponseEntity.ok(schedules);


    }

    @PutMapping("/schedule/{schedule_id}/user/{user_id}")
    public ResponseEntity<Schedule> updateSchedule(
            @PathVariable long schedule_id,
            @PathVariable long user_id,
            @RequestBody ScheduleRequest scheduleRequest) throws NotYourScheduleException {
        Schedule updatedSchedule = scheduleService.updateSchedule(schedule_id, user_id, scheduleRequest);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/schedule/{schedule_id}/user/{user_id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable long user_id,@PathVariable long schedule_id) throws NotYourScheduleException {
        scheduleService.deleteSchedule(user_id,schedule_id);
        return ResponseEntity.noContent().build();
    }
}
