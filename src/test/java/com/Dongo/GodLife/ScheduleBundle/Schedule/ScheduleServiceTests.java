package com.Dongo.GodLife.ScheduleBundle.Schedule;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Dto.ScheduleRequest;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.ScheduleService;
import com.Dongo.GodLife.User.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTests {

    @InjectMocks
    ScheduleService sut;

    @Spy
    SchedulePersistenceAdapterStub scheduleRepository;

    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("test1@test.com");
        user1.setId(1L);
        user2 = new User("test2@test.com");
        user2.setId(2L);
    }

    @Nested
    class CreateSchedule {
        @Test
        @DisplayName("새로운 스케줄을 생성하고 저장한다")
        void testCreateSchedule() {
            // given
            LocalDateTime startTime = LocalDateTime.now().plusHours(1);
            LocalDateTime endTime = startTime.plusHours(2);
            ScheduleRequest scheduleRequest = new ScheduleRequest("일정 제목", startTime, endTime);

            // when
            Schedule result = sut.createSchedule(scheduleRequest, user1);

            // then
            assertNotNull(result);
            assertEquals("일정 제목", result.getScheduleTitle());
            assertEquals(startTime, result.getStartTime());
            assertEquals(endTime, result.getEndTime());
            assertEquals(user1, result.getUser());
        }
    }

    @Nested
    class GetAllSchedulesByUserId {
        @Test
        @DisplayName("사용자의 모든 스케줄을 페이지 단위로 가져온다")
        void testGetAllSchedulesByUserId() throws NotYourScheduleException {
            // given
            LocalDateTime startTime = LocalDateTime.now().plusHours(1);
            LocalDateTime endTime = startTime.plusHours(2);
            ScheduleRequest scheduleRequest = new ScheduleRequest("일정 제목", startTime, endTime);
            sut.createSchedule(scheduleRequest, user1);

            Pageable pageable = PageRequest.of(0, 10);

            // when
            Page<Schedule> result = sut.getAllschedulesByUserId(user1, pageable);

            // then
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals("일정 제목", result.getContent().get(0).getScheduleTitle());
        }
    }

    @Nested
    class UpdateSchedule {
        @Test
        @DisplayName("자신의 스케줄을 업데이트한다")
        void testUpdateOwnSchedule() throws NotYourScheduleException {
            // given
            LocalDateTime startTime = LocalDateTime.now().plusHours(1);
            LocalDateTime endTime = startTime.plusHours(2);
            Schedule existingSchedule = Schedule.builder()
                .scheduleId(1L)
                .user(user1)
                .scheduleTitle("원래 제목")
                .startTime(startTime)
                .endTime(endTime)
                .build();
            when(scheduleRepository.findById(1L)).thenReturn(java.util.Optional.of(existingSchedule));

            LocalDateTime newStartTime = LocalDateTime.now().plusHours(3);
            LocalDateTime newEndTime = newStartTime.plusHours(2);
            ScheduleRequest scheduleRequest = new ScheduleRequest("수정된 제목", newStartTime, newEndTime);

            // when
            Schedule result = sut.updateSchedule(1L, user1.getId(), scheduleRequest);

            // then
            assertEquals("수정된 제목", result.getScheduleTitle());
            assertEquals(newStartTime, result.getStartTime());
            assertEquals(newEndTime, result.getEndTime());
        }

        @Test
        @DisplayName("다른 사용자의 스케줄을 수정하려고 하면 예외가 발생한다")
        void testUpdateOthersSchedule() {
            // given
            LocalDateTime startTime = LocalDateTime.now().plusHours(1);
            LocalDateTime endTime = startTime.plusHours(2);
            Schedule existingSchedule = Schedule.builder()
                .scheduleId(1L)
                .user(user2)
                .scheduleTitle("원래 제목")
                .startTime(startTime)
                .endTime(endTime)
                .build();
            when(scheduleRepository.findById(1L)).thenReturn(java.util.Optional.of(existingSchedule));

            ScheduleRequest scheduleRequest = new ScheduleRequest("수정된 제목", startTime, endTime);

            // when & then
            assertThrows(NotYourScheduleException.class,
                () -> sut.updateSchedule(1L, user1.getId(), scheduleRequest));
        }
    }

    @Nested
    class DeleteSchedule {
        @Test
        @DisplayName("자신의 스케줄을 삭제한다")
        void testDeleteOwnSchedule() throws NotYourScheduleException {
            // given
            Schedule existingSchedule = Schedule.builder()
                .scheduleId(1L)
                .user(user1)
                .build();
            when(scheduleRepository.findById(1L)).thenReturn(java.util.Optional.of(existingSchedule));

            // when
            sut.deleteSchedule(user1.getId(), 1L);

            // then
            verify(scheduleRepository).delete(existingSchedule);
        }

        @Test
        @DisplayName("다른 사용자의 스케줄을 삭제하려고 하면 예외가 발생한다")
        void testDeleteOthersSchedule() {
            // given
            Schedule existingSchedule = Schedule.builder()
                .scheduleId(1L)
                .user(user2)
                .build();
            when(scheduleRepository.findById(1L)).thenReturn(java.util.Optional.of(existingSchedule));

            // when & then
            assertThrows(NotYourScheduleException.class,
                () -> sut.deleteSchedule(user1.getId(), 1L));
        }
    }
} 