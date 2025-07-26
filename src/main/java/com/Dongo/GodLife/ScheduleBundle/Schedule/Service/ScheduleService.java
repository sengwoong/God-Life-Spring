package com.Dongo.GodLife.ScheduleBundle.Schedule.Service;



import com.Dongo.GodLife.ScheduleBundle.Schedule.Dto.ScheduleRequest;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.Validator;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final  SchedulePersistenceAdapter scheduleRepository;

    public Schedule createSchedule(ScheduleRequest request, User user) {
        Schedule schedule = new Schedule();
        schedule.setTitle(request.getScheduleTitle());
        schedule.setContent(request.getContent());
        schedule.setTime(request.getStartTime());
        schedule.setDay(request.getDay());
        schedule.setHasAlarm(request.isHasAlarm());
        schedule.setUser(user);
        
        return scheduleRepository.save(schedule);
    }

    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));
    }

    public Page<Schedule> getAllschedulesByUserId(User user, Pageable pageable) throws NotYourScheduleException {
        return scheduleRepository.findByUser(user, pageable);
    }

    public Schedule updateSchedule(Long scheduleId, ScheduleRequest scheduleRequest, User user) throws  NotYourScheduleException {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with ID: " + scheduleId));

        Validator.validateNotEmpty(scheduleRequest.getScheduleTitle(), "Schedule title cannot be empty");
        Validator.validateNotEmpty(scheduleRequest.getStartTime(), "Schedule StartTime cannot be empty");
        Validator.validateNotEmpty(scheduleRequest.getEndTime(), "Schedule EndTime cannot be empty");

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new NotYourScheduleException("Access denied: User does not own the schedule");
        }

        schedule.setTitle(scheduleRequest.getScheduleTitle());
        schedule.setContent(scheduleRequest.getContent());
        schedule.setTime(scheduleRequest.getStartTime());
        schedule.setDay(scheduleRequest.getDay());
        schedule.setHasAlarm(scheduleRequest.isHasAlarm());
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long scheduleId, User user) throws  NotYourScheduleException {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));
        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new NotYourScheduleException("Access denied: User does not own the schedule");
        }
        scheduleRepository.delete(schedule);
    }
}
