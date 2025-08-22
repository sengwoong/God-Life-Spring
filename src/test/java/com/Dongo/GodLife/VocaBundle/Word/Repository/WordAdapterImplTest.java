package com.Dongo.GodLife.VocaBundle.Word.Repository;

import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
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
class WordAdapterImplTest {

    @Mock
    private WordRepository wordRepository;

    @InjectMocks
    private WordAdapterImpl wordAdapter;

    @Test
    @DisplayName("save/findById 위임")
    void saveAndFindById() {
        Word w = new Word();
        w.setWordId(1L);
        w.setWord("A");
        given(wordRepository.save(w)).willReturn(w);
        given(wordRepository.findById(1L)).willReturn(Optional.of(w));

        assertEquals(1L, wordAdapter.save(w).getWordId());
        assertTrue(wordAdapter.findById(1L).isPresent());
        verify(wordRepository).save(w);
        verify(wordRepository).findById(1L);
    }

    @Test
    @DisplayName("getAllWordsByVocaId 위임")
    void getAllWordsByVocaId() {
        PageRequest pageable = PageRequest.of(0, 10);
        Word w1 = new Word();
        w1.setWordId(1L);
        Page<Word> page = new PageImpl<>(List.of(w1));
        given(wordRepository.getAllWordsByVocaVocaId(5L, pageable)).willReturn(page);

        assertEquals(1, wordAdapter.getAllWordsByVocaId(5L, pageable).getTotalElements());
    }

    @Test
    @DisplayName("delete: 존재하지 않으면 null 반환, 존재하면 삭제")
    void deleteBehavior() {
        given(wordRepository.findById(7L)).willReturn(Optional.empty());
        assertNull(wordAdapter.delete(7L));

        Word w = new Word();
        w.setWordId(8L);
        given(wordRepository.findById(8L)).willReturn(Optional.of(w));
        Word deleted = wordAdapter.delete(8L);
        assertEquals(8L, deleted.getWordId());
        verify(wordRepository).deleteById(8L);
    }
}


