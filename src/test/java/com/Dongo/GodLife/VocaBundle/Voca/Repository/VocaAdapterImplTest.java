package com.Dongo.GodLife.VocaBundle.Voca.Repository;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
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
class VocaAdapterImplTest {

    @Mock
    private VocaRepository vocaRepository;

    @InjectMocks
    private VocaAdapterImpl vocaAdapter;

    @Test
    @DisplayName("save/findById/delete 위임")
    void basicDelegations() {
        Voca v = Voca.builder().vocaId(1L).build();
        given(vocaRepository.save(v)).willReturn(v);
        given(vocaRepository.findById(1L)).willReturn(Optional.of(v));

        assertEquals(1L, vocaAdapter.save(v).getVocaId());
        assertTrue(vocaAdapter.findById(1L).isPresent());

        Voca deleted = vocaAdapter.delete(v);
        assertEquals(1L, deleted.getVocaId());
        verify(vocaRepository).delete(v);
    }

    @Test
    @DisplayName("페이지 조회 위임: findByUser/findByUserAndIsShared")
    void pageDelegations() {
        PageRequest pageable = PageRequest.of(0, 10);
        User user = User.builder().id(3L).build();
        Page<Voca> page = new PageImpl<>(List.of(Voca.builder().vocaId(1L).build()));

        given(vocaRepository.findByUser(user, pageable)).willReturn(page);
        given(vocaRepository.findByUserAndIsShared(user, true, pageable)).willReturn(page);

        assertEquals(1, vocaAdapter.findByUser(user, pageable).getTotalElements());
        assertEquals(1, vocaAdapter.findByUserAndIsShared(user, true, pageable).getTotalElements());
    }
}


