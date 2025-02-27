package com.Dongo.GodLife.ScheduleBundle.Schedule;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Controller.ScheduleController;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Dto.ScheduleRequest;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("스케줄 관리")
    @Nested
    class ScheduleManagement {

        @DisplayName("스케줄 생성")
        @Nested
        class CreateSchedule {
            @Test
            @DisplayName("정상적인 요청일 경우, 200 status와 Schedule을 반환한다")
            void createSchedule_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                LocalDateTime startTime = LocalDateTime.now().plusHours(1);
                LocalDateTime endTime = startTime.plusHours(2);
                ScheduleRequest scheduleRequest = new ScheduleRequest("일정 제목", startTime, endTime);

                Schedule schedule = Schedule.builder()
                    .scheduleTitle("일정 제목")
                    .startTime(startTime)
                    .endTime(endTime)
                    .user(user)
                    .build();

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(scheduleService.createSchedule(any(ScheduleRequest.class), any(User.class)))
                    .willReturn(schedule);

                // when & then
                mockMvc.perform(post("/schedules/user/{user_id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.scheduleTitle").value("일정 제목"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(scheduleService).createSchedule(any(ScheduleRequest.class), any(User.class));
            }
        }

        @DisplayName("스케줄 조회")
        @Nested
        class GetSchedules {
            @Test
            @DisplayName("사용자의 스케줄 목록을 조회한다")
            void getSchedulesByUserId_Success() throws Exception ,NotYourScheduleException {
                // given
                User user = new User("test@test.com");
                LocalDateTime startTime = LocalDateTime.now().plusHours(1);
                LocalDateTime endTime = startTime.plusHours(2);
                
                Schedule schedule = Schedule.builder()
                    .scheduleTitle("일정 제목")
                    .startTime(startTime)
                    .endTime(endTime)
                    .user(user)
                    .build();

                Page<Schedule> schedulePage = new PageImpl<>(List.of(schedule));

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(scheduleService.getAllschedulesByUserId(any(User.class), any()))
                    .willReturn(schedulePage);

                // when & then
                mockMvc.perform(get("/schedules/user/{user_id}", 1L)
                        .param("page", "0")
                        .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].scheduleTitle").value("일정 제목"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(scheduleService).getAllschedulesByUserId(any(User.class), any());
            }
        }

        @DisplayName("스케줄 수정")
        @Nested
        class UpdateSchedule {
            @Test
            @DisplayName("정상적인 요청일 경우, 수정된 Schedule을 반환한다")
            void updateSchedule_Success() throws Exception ,NotYourScheduleException {
                // given
                LocalDateTime startTime = LocalDateTime.now().plusHours(1);
                LocalDateTime endTime = startTime.plusHours(2);
                ScheduleRequest scheduleRequest = new ScheduleRequest("수정된 제목", startTime, endTime);

                Schedule updatedSchedule = Schedule.builder()
                    .scheduleTitle("수정된 제목")
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();

                given(scheduleService.updateSchedule(anyLong(), anyLong(), any(ScheduleRequest.class)))
                    .willReturn(updatedSchedule);

                // when & then
                mockMvc.perform(put("/schedules/schedule/{schedule_id}/user/{user_id}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.scheduleTitle").value("수정된 제목"));

                verify(scheduleService).updateSchedule(anyLong(), anyLong(), any(ScheduleRequest.class));
            }
        }

        @DisplayName("스케줄 삭제")
        @Nested
        class DeleteSchedule {
            @Test
            @DisplayName("정상적인 요청일 경우, 204 status를 반환한다")
            void deleteSchedule_Success() throws Exception ,NotYourScheduleException {
                // when & then
                mockMvc.perform(delete("/schedules/schedule/{schedule_id}/user/{user_id}", 1L, 1L))
                    .andExpect(status().isNoContent());

                verify(scheduleService).deleteSchedule(anyLong(), anyLong());
            }
        }
    }
} 