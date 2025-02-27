package com.Dongo.GodLife.ScheduleBundle.Alarm;

import com.Dongo.GodLife.ScheduleBundle.Alarm.Controller.AlarmController;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Dto.AlarmRequest;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Model.Alarm;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Service.AlarmService;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.ScheduleService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.Dongo.GodLife.ScheduleBundle.Alarm.Exception.NotYourAlarmException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;


@WebMvcTest(AlarmController.class)
class AlarmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlarmService alarmService;

    @MockBean
    private UserService userService;

    @MockBean
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("알람 관리")
    @Nested
    class AlarmManagement {

        @DisplayName("알람 생성")
        @Nested
        class CreateAlarm {
            @Test
            @DisplayName("정상적인 요청일 경우, 200 status와 Alarm을 반환한다")
            void createAlarm_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                Schedule schedule = new Schedule();
                AlarmRequest alarmRequest = new AlarmRequest("알람 제목", "알람 내용");

                Alarm alarm = Alarm.builder()
                    .alarmTitle("알람 제목")
                    .alarmcontent("알람 내용")
                    .user(user)
                    .schedule(schedule)
                    .build();

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(scheduleService.getScheduleById(anyLong())).willReturn(schedule);
                given(alarmService.createAlarm(any(AlarmRequest.class), any(Schedule.class), any(User.class)))
                    .willReturn(alarm);

                // when & then
                mockMvc.perform(post("/alarms/schedule/{schedule_id}/users/{user_id}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alarmRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.alarmTitle").value("알람 제목"))
                    .andExpect(jsonPath("$.alarmcontent").value("알람 내용"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(scheduleService).getScheduleById(anyLong());
                verify(alarmService).createAlarm(any(AlarmRequest.class), any(Schedule.class), any(User.class));
            }
        }

        @DisplayName("알람 조회")
        @Nested
        class GetAlarm {
            @Test
            @DisplayName("알람 ID로 조회 시 알람을 반환한다")
            void getAlarm_Success() throws Exception {
                // given
                Alarm alarm = Alarm.builder()
                    .alarmTitle("알람 제목")
                    .alarmcontent("알람 내용")
                    .build();

                given(alarmService.getAlarmByID(anyLong())).willReturn(alarm);

                // when & then
                mockMvc.perform(get("/alarms/alarm/{alarm_id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.alarmTitle").value("알람 제목"))
                    .andExpect(jsonPath("$.alarmcontent").value("알람 내용"));

                verify(alarmService).getAlarmByID(anyLong());
            }
        }

        @DisplayName("알람 수정")
        @Nested
        class UpdateAlarm {
            @Test
            @DisplayName("정상적인 요청일 경우, 수정된 알람을 반환한다")
            void updateAlarm_Success() throws Exception ,NotYourAlarmException, NotYourScheduleException {
                // given
                AlarmRequest alarmRequest = new AlarmRequest("수정된 제목", "수정된 내용");

                Alarm updatedAlarm = Alarm.builder()
                    .alarmTitle("수정된 제목")
                    .alarmcontent("수정된 내용")
                    .build();

                given(alarmService.updateAlarm(anyLong(), anyLong(), any(AlarmRequest.class)))
                    .willReturn(updatedAlarm);

                // when & then
                mockMvc.perform(put("/alarms/alarm/{alarm_id}/user/{user_id}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alarmRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.alarmTitle").value("수정된 제목"))
                    .andExpect(jsonPath("$.alarmcontent").value("수정된 내용"));

                verify(alarmService).updateAlarm(anyLong(), anyLong(), any(AlarmRequest.class));
            }
        }

        @DisplayName("알람 삭제")
        @Nested
        class DeleteAlarm {
            @Test
            @DisplayName("정상적인 요청일 경우, 200 status를 반환한다")
            void deleteAlarm_Success() throws Exception ,NotYourAlarmException, NotYourScheduleException {
                // given
                User user = new User("test@test.com");
                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);

                // when & then
                mockMvc.perform(delete("/alarms/alarm/{alarm_id}/user/{user_id}", 1L, 1L)
                        .param("user_id", "1"))
                    .andExpect(status().isOk());

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(alarmService).deleteAlarm(any(User.class), anyLong());
            }
        }
    }
} 