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

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before or equal to end time");
        }
        Schedule schedule = new Schedule();
        schedule.setScheduleTitle(request.getScheduleTitle());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
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

    public Schedule updateSchedule(long scheduleId, long userId , ScheduleRequest scheduleRequest) throws  NotYourScheduleException {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with ID: " + scheduleId));

        Validator.validateNotEmpty(scheduleRequest.getScheduleTitle(), "Schedule title cannot be empty");
        Validator.validateNotEmpty(scheduleRequest.getScheduleTitle(), "Schedule StartTime cannot be empty");
        Validator.validateNotEmpty(scheduleRequest.getScheduleTitle(), "Schedule EndTime cannot be empty");

        if (!schedule.getUser().getId().equals(userId)) {
            throw new NotYourScheduleException("Access denied: User does not own the schedule");
        }

        schedule.setScheduleTitle(scheduleRequest.getScheduleTitle());
        schedule.setStartTime(scheduleRequest.getStartTime());
        schedule.setEndTime(scheduleRequest.getEndTime());
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(long userId ,long scheduleId) throws  NotYourScheduleException {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));
        if (!schedule.getUser().getId().equals(userId)) {
            throw new NotYourScheduleException("Access denied: User does not own the schedule");
        }
        scheduleRepository.delete(schedule);
    }
}
