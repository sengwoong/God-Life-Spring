package com.Dongo.GodLife.ScheduleBundle.Alarm;

import com.Dongo.GodLife.ScheduleBundle.Alarm.Dto.AlarmRequest;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Exception.NotYourAlarmException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Service.AlarmService;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.User.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlarmServiceTests {

    @InjectMocks
    AlarmService sut;

    @Spy
    AlarmPersistenceAdapterStub alarmRepository;

    User user1;
    User user2;
    Schedule schedule;

    @BeforeEach
    void setUp() {
        user1 = new User("test1@test.com");
        user1.setId(1L);
        user2 = new User("test2@test.com");
        user2.setId(2L);
        schedule = new Schedule();
    }

    @Nested
    class CreateAlarm {
        @Test
        @DisplayName("새로운 알람을 생성하고 저장한다")
        void testCreateAlarm() {
            // given
            AlarmRequest alarmRequest = new AlarmRequest("알람 제목", "알람 내용");

            // when
            Alarm result = sut.createAlarm(alarmRequest, schedule, user1);

            // then
            assertNotNull(result);
            assertEquals("알람 제목", result.getAlarmTitle());
            assertEquals("알람 내용", result.getAlarmcontent());
            assertEquals(user1, result.getUser());
            assertEquals(schedule, result.getSchedule());
        }
    }

    @Nested
    class UpdateAlarm {
        @Test
        @DisplayName("자신의 알람을 업데이트한다")
        void testUpdateOwnAlarm() throws NotYourAlarmException {
            // given
            Alarm existingAlarm = Alarm.builder()
                .alarmId(1L)
                .user(user1)
                .alarmTitle("원래 제목")
                .alarmcontent("원래 내용")
                .build();
            when(alarmRepository.findById(1L)).thenReturn(java.util.Optional.of(existingAlarm));

            AlarmRequest alarmRequest = new AlarmRequest("수정된 제목", "수정된 내용");

            // when
            Alarm result = sut.updateAlarm(1L, user1.getId(), alarmRequest);

            // then
            assertEquals("수정된 제목", result.getAlarmTitle());
            assertEquals("수정된 내용", result.getAlarmcontent());
        }

        @Test
        @DisplayName("다른 사용자의 알람을 수정하려고 하면 예외가 발생한다")
        void testUpdateOthersAlarm() {
            // given
            Alarm existingAlarm = Alarm.builder()
                .alarmId(1L)
                .user(user2)
                .build();
            when(alarmRepository.findById(1L)).thenReturn(java.util.Optional.of(existingAlarm));

            AlarmRequest alarmRequest = new AlarmRequest("수정된 제목", "수정된 내용");

            // when & then
            assertThrows(NotYourAlarmException.class, 
                () -> sut.updateAlarm(1L, user1.getId(), alarmRequest));
        }
    }

    @Nested
    class DeleteAlarm {
        @Test
        @DisplayName("자신의 알람을 삭제한다")
        void testDeleteOwnAlarm() throws NotYourAlarmException , NotYourScheduleException{
            // given
            Alarm existingAlarm = Alarm.builder()
                .alarmId(1L)
                .user(user1)
                .build();
            when(alarmRepository.findById(1L)).thenReturn(java.util.Optional.of(existingAlarm));

            // when
            sut.deleteAlarm(user1, 1L);

            // then
            verify(alarmRepository).delete(existingAlarm);
        }

        @Test
        @DisplayName("다른 사용자의 알람을 삭제하려고 하면 예외가 발생한다")
        void testDeleteOthersAlarm() {
            // given
            Alarm existingAlarm = Alarm.builder()
                .alarmId(1L)
                .user(user2)
                .build();
            when(alarmRepository.findById(1L)).thenReturn(java.util.Optional.of(existingAlarm));

            // when & then
            assertThrows(NotYourAlarmException.class, 
                () -> sut.deleteAlarm(user1, 1L));
        }
    }
} 