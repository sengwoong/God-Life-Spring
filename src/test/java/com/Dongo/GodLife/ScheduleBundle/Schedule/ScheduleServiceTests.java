package com.Dongo.GodLife.ScheduleBundle.Schedule;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Dto.ScheduleRequest;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.ScheduleService;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.SchedulePersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTests {

    @Mock
    private SchedulePersistenceAdapter scheduleRepository;

    @InjectMocks
    private ScheduleService sut;

    private User user1;
    private User user2;
    private ScheduleRequest scheduleRequest;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .email("user1@test.com")
                .nickName("user1")
                .build();

        user2 = User.builder()
                .id(2L)
                .email("user2@test.com")
                .nickName("user2")
                .build();

        scheduleRequest = new ScheduleRequest(
                "일정 제목", "일정 내용", "09:00", "10:00", "2024-01-01", false
        );
    }

    @Nested
    class CreateSchedule {
        @Test
        @DisplayName("새로운 스케줄을 생성하고 저장한다")
        void testCreateSchedule() {
            // given
            Schedule schedule = Schedule.builder()
                    .scheduleId(1L)
                    .title("일정 제목")
                    .content("일정 내용")
                    .startTime("09:00")
                    .endTime("10:00")
                    .day("2024-01-01")
                    .hasAlarm(false)
                    .user(user1)
                    .build();

            given(scheduleRepository.save(any(Schedule.class))).willReturn(schedule);

            // when
            Schedule result = sut.createSchedule(scheduleRequest, user1);

            // then
            assertNotNull(result);
            assertEquals("일정 제목", result.getTitle());
            assertEquals("일정 내용", result.getContent());
            assertEquals("09:00", result.getStartTime());
            assertEquals("10:00", result.getEndTime());
            assertEquals("2024-01-01", result.getDay());
            assertFalse(result.isHasAlarm());
            assertEquals(user1, result.getUser());
            verify(scheduleRepository).save(any(Schedule.class));
        }
    }

    @Nested
    class GetSchedule {
        @Test
        @DisplayName("사용자의 스케줄 목록을 조회한다")
        void testGetAllSchedulesByUserId() throws NotYourScheduleException {
            // given
            Schedule schedule = Schedule.builder()
                    .scheduleId(1L)
                    .title("일정 제목")
                    .content("일정 내용")
                    .startTime("09:00")
                    .endTime("10:00")
                    .day("2024-01-01")
                    .user(user1)
                    .build();

            Page<Schedule> schedulePage = new PageImpl<>(List.of(schedule));
            given(scheduleRepository.findByUser(user1, Pageable.unpaged())).willReturn(schedulePage);

            // when
            Page<Schedule> result = sut.getAllschedulesByUserId(user1, Pageable.unpaged());

            // then
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("일정 제목", result.getContent().get(0).getTitle());
            assertEquals("일정 내용", result.getContent().get(0).getContent());
        }
    }

    @Nested
    class UpdateSchedule {
        @Test
        @DisplayName("스케줄을 성공적으로 수정한다")
        void testUpdateSchedule() throws NotYourScheduleException {
            // given
            Schedule existingSchedule = Schedule.builder()
                    .scheduleId(1L)
                    .title("기존 제목")
                    .content("기존 내용")
                    .startTime("09:00")
                    .endTime("10:00")
                    .day("2024-01-01")
                    .user(user1)
                    .build();

            ScheduleRequest updateRequest = new ScheduleRequest(
                    "수정된 제목", "수정된 내용", "10:00", "11:00", "2024-01-01", true
            );

            given(scheduleRepository.findById(1L)).willReturn(java.util.Optional.of(existingSchedule));
            given(scheduleRepository.save(any(Schedule.class))).willReturn(existingSchedule);

            // when
            Schedule result = sut.updateSchedule(1L, updateRequest, user1);

            // then
            assertNotNull(result);
            assertEquals("수정된 제목", result.getTitle());
            assertEquals("수정된 내용", result.getContent());
            assertEquals("10:00", result.getStartTime());
            assertEquals("11:00", result.getEndTime());
            assertEquals("2024-01-01", result.getDay());
            assertTrue(result.isHasAlarm());
            verify(scheduleRepository).save(any(Schedule.class));
        }

        @Test
        @DisplayName("다른 사용자의 스케줄을 수정하려고 하면 예외가 발생한다")
        void testUpdateScheduleNotOwner() {
            // given
            Schedule existingSchedule = Schedule.builder()
                    .scheduleId(1L)
                    .title("기존 제목")
                    .user(user1)
                    .build();

            ScheduleRequest updateRequest = new ScheduleRequest(
                    "수정된 제목", "수정된 내용", "10:00", "11:00", "2024-01-01", true
            );

            given(scheduleRepository.findById(1L)).willReturn(java.util.Optional.of(existingSchedule));

            // when & then
            assertThrows(NotYourScheduleException.class,
                    () -> sut.updateSchedule(1L, updateRequest, user2));
            verify(scheduleRepository, never()).save(any(Schedule.class));
        }
    }

    @Nested
    class DeleteSchedule {
        @Test
        @DisplayName("스케줄을 성공적으로 삭제한다")
        void testDeleteSchedule() throws NotYourScheduleException {
            // given
            Schedule existingSchedule = Schedule.builder()
                    .scheduleId(1L)
                    .title("삭제할 스케줄")
                    .user(user1)
                    .build();

            given(scheduleRepository.findById(1L)).willReturn(java.util.Optional.of(existingSchedule));

            // when
            sut.deleteSchedule(1L, user1);

            // then
            verify(scheduleRepository).delete(existingSchedule);
        }

        @Test
        @DisplayName("다른 사용자의 스케줄을 삭제하려고 하면 예외가 발생한다")
        void testDeleteScheduleNotOwner() throws NotYourScheduleException {
            // given
            Schedule existingSchedule = Schedule.builder()
                    .scheduleId(1L)
                    .title("삭제할 스케줄")
                    .user(user1)
                    .build();

            given(scheduleRepository.findById(1L)).willReturn(java.util.Optional.of(existingSchedule));

            // when & then
            assertThrows(NotYourScheduleException.class,
                    () -> sut.deleteSchedule(1L, user2));
            verify(scheduleRepository, never()).delete(any(Schedule.class));
        }
    }
} 