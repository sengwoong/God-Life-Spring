package com.Dongo.GodLife.ScheduleBundle.Schedule.Repository;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.User.Model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScheduleAdapterImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleAdapterImpl scheduleAdapter;

    @Test
    @DisplayName("save/findById/delete 위임")
    void basicDelegations() {
        Schedule s = Schedule.builder().scheduleId(1L).build();
        given(scheduleRepository.save(s)).willReturn(s);
        given(scheduleRepository.findById(1L)).willReturn(Optional.of(s));

        assertEquals(1L, scheduleAdapter.save(s).getScheduleId());
        assertTrue(scheduleAdapter.findById(1L).isPresent());

        Schedule deleted = scheduleAdapter.delete(s);
        assertEquals(1L, deleted.getScheduleId());
        verify(scheduleRepository).delete(s);
    }

    @Test
    @DisplayName("findByUser 페이지 조회 위임")
    void pageDelegation() {
        PageRequest pageable = PageRequest.of(0, 10);
        User user = User.builder().id(3L).build();
        Page<Schedule> page = new PageImpl<>(List.of(Schedule.builder().scheduleId(1L).build()));

        given(scheduleRepository.findByUser(user, pageable)).willReturn(page);
        assertEquals(1, scheduleAdapter.findByUser(user, pageable).getTotalElements());
    }
}


