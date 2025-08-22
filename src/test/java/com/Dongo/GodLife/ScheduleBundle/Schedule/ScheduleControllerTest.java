package com.Dongo.GodLife.ScheduleBundle.Schedule;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Controller.ScheduleController;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Dto.ScheduleRequest;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Exception.NotYourScheduleException;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.ScheduleBundle.Schedule.Service.ScheduleService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ScheduleController scheduleController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        objectMapper = mapper;
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController)
                .setCustomArgumentResolvers(new org.springframework.data.web.PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(mapper))
                .build();
    }

    @Nested
    class ScheduleManagement {
        @Test
        @DisplayName("새로운 스케줄을 생성한다")
        void testCreateSchedule() throws Exception {
            // given
            User user = User.builder().id(1L).email("test@test.com").build();
            ScheduleRequest request = new ScheduleRequest(
                "일정 제목", "일정 내용", "09:00", "10:00", "2024-01-01", false
            );

            Schedule schedule = Schedule.builder()
                .scheduleId(1L)
                .title("일정 제목")
                .content("일정 내용")
                .startTime("09:00")
                .endTime("10:00")
                .day("2024-01-01")
                .hasAlarm(false)
                .user(user)
                .build();

            given(userService.CheckUserAndGetUser(1L)).willReturn(user);
            given(scheduleService.createSchedule(any(ScheduleRequest.class), any(User.class)))
                .willReturn(schedule);

            // when & then
            mockMvc.perform(post("/schedules/user/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("일정 제목"));

            verify(scheduleService).createSchedule(any(ScheduleRequest.class), any(User.class));
        }

        @Test
        @DisplayName("사용자의 스케줄 목록을 조회한다")
        void testGetSchedulesByUserId() throws Exception, NotYourScheduleException {
            // given
            User user = User.builder().id(1L).email("test@test.com").build();
            Schedule schedule = Schedule.builder()
                .scheduleId(1L)
                .title("일정 제목")
                .content("일정 내용")
                .startTime("09:00")
                .endTime("10:00")
                .day("2024-01-01")
                .user(user)
                .build();

            Page<Schedule> schedulePage = new PageImpl<>(List.of(schedule));

            given(userService.CheckUserAndGetUser(org.mockito.ArgumentMatchers.eq(1L))).willReturn(user);
            given(scheduleService.getAllschedulesByUserId(org.mockito.ArgumentMatchers.any(User.class), org.mockito.ArgumentMatchers.any(Pageable.class)))
                .willReturn(schedulePage);

            // when & then
            mockMvc.perform(get("/schedules/user/1").param("page", "0").param("size", "10"))
                .andExpect(status().isOk());

            verify(scheduleService).getAllschedulesByUserId(org.mockito.ArgumentMatchers.any(User.class), org.mockito.ArgumentMatchers.any(Pageable.class));
        }

        @Test
        @DisplayName("스케줄을 업데이트한다")
        void testUpdateSchedule() throws Exception, NotYourScheduleException {
            // given
            User user = User.builder().id(1L).email("test@test.com").build();
            ScheduleRequest request = new ScheduleRequest(
                "수정된 제목", "수정된 내용", "10:00", "11:00", "2024-01-01", true
            );

            Schedule updatedSchedule = Schedule.builder()
                .scheduleId(1L)
                .title("수정된 제목")
                .content("수정된 내용")
                .startTime("10:00")
                .endTime("11:00")
                .day("2024-01-01")
                .hasAlarm(true)
                .user(user)
                .build();

            given(userService.CheckUserAndGetUser(1L)).willReturn(user);
            given(scheduleService.updateSchedule(eq(1L), any(ScheduleRequest.class), eq(user)))
                .willReturn(updatedSchedule);

            // when & then
            mockMvc.perform(put("/schedules/schedule/1/user/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목"));

            verify(userService).CheckUserAndGetUser(1L);
            verify(scheduleService).updateSchedule(eq(1L), any(ScheduleRequest.class), eq(user));
        }

        @Test
        @DisplayName("스케줄을 삭제한다")
        void testDeleteSchedule() throws Exception, NotYourScheduleException {
            // given
            User user = User.builder().id(1L).email("test@test.com").build();
            given(userService.CheckUserAndGetUser(org.mockito.ArgumentMatchers.eq(1L))).willReturn(user);

            // when & then
            mockMvc.perform(delete("/schedules/schedule/1/user/1"))
                .andExpect(status().isNoContent());

            verify(scheduleService).deleteSchedule(org.mockito.ArgumentMatchers.eq(1L), any(User.class));
        }
    }
} 