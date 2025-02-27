package com.Dongo.GodLife.VocaBundle.Word;

import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.VocaBundle.Voca.Service.VocaPersistenceAdapter;
import com.Dongo.GodLife.VocaBundle.Word.Dto.WordRequest;
import com.Dongo.GodLife.VocaBundle.Word.Exception.NotYourWordException;
import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import com.Dongo.GodLife.VocaBundle.Word.Service.WordPersistenceAdapter;
import com.Dongo.GodLife.VocaBundle.Word.Service.WordService;
import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.EntityNotFoundException;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WordServiceTests {

    @InjectMocks
    private WordService wordService;

    @Mock
    private WordPersistenceAdapter wordRepository;

    @Mock
    private VocaPersistenceAdapter vocaRepository;

    private User user;
    private Voca voca;

    @BeforeEach
    void setUp() {
        user = new User("test@test.com");
        user.setId(1L);
        voca = new Voca();
        voca.setVocaId(1L);
        voca.setUser(user);
    }

    @Nested
    class SaveWord {
        @Test
        @DisplayName("새로운 Word를 생성하고 저장한다")
        void saveWord_Success() {
            // given
            WordRequest wordRequest = new WordRequest();
            wordRequest.setWord("apple");
            wordRequest.setMeaning("사과");
            wordRequest.setVocaId(1L);

            Word word = new Word();
            word.setWord("apple");
            word.setMeaning("사과");

            when(vocaRepository.findById(anyLong())).thenReturn(Optional.of(voca));
            when(wordRepository.save(any(Word.class))).thenReturn(word);

            // when
            Word result = wordService.saveWord(wordRequest);

            // then
            assertNotNull(result);
            assertEquals("apple", result.getWord());
            assertEquals("사과", result.getMeaning());
            verify(vocaRepository).findById(anyLong());
            verify(wordRepository).save(any(Word.class));
        }

        @Test
        @DisplayName("존재하지 않는 Voca ID로 Word를 생성하려 할 경우 예외가 발생한다")
        void saveWord_WithInvalidVocaId() {
            // given
            WordRequest wordRequest = new WordRequest();
            wordRequest.setVocaId(999L);

            when(vocaRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when & then
            assertThrows(EntityNotFoundException.class, () -> wordService.saveWord(wordRequest));
        }
    }

    @Nested
    class GetAllWordsByVocaId {
        @Test
        @DisplayName("Voca ID로 모든 Word를 조회한다")
        void getAllWordsByVocaId_Success() {
            // given
            Word word = new Word();
            word.setWord("apple");
            word.setMeaning("사과");
            Page<Word> wordPage = new PageImpl<>(Collections.singletonList(word));

            when(wordRepository.getAllWordsByVocaId(anyLong(), any(Pageable.class))).thenReturn(wordPage);

            // when
            Page<Word> result = wordService.getAllwordsByVocaId(1L, Pageable.unpaged());

            // then
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("apple", result.getContent().get(0).getWord());
        }
    }

    @Nested
    class UpdateWord {
        @Test
        @DisplayName("Word를 성공적으로 수정한다")
        void updateWord_Success() {
            // given
            Word existingWord = new Word();
            existingWord.setVoca(voca);
            
            WordRequest wordRequest = new WordRequest();
            wordRequest.setWord("updated");
            wordRequest.setMeaning("수정됨");

            when(wordRepository.findById(anyLong())).thenReturn(Optional.of(existingWord));
            when(wordRepository.save(any(Word.class))).thenReturn(existingWord);

            // when
            Word result = wordService.updateWord(1L, user.getId(), wordRequest);

            // then
            assertNotNull(result);
            assertEquals("updated", result.getWord());
            assertEquals("수정됨", result.getMeaning());
        }

        @Test
        @DisplayName("다른 사용자의 Word를 수정하려고 하면 예외가 발생한다")
        void updateWord_NotYourWord() {
            // given
            Word existingWord = new Word();
            existingWord.setVoca(voca);
            
            WordRequest wordRequest = new WordRequest();
            
            when(wordRepository.findById(anyLong())).thenReturn(Optional.of(existingWord));

            // when & then
            assertThrows(NotYourWordException.class, 
                () -> wordService.updateWord(1L, 999L, wordRequest));
        }
    }

    @Nested
    class DeleteWord {
        @Test
        @DisplayName("Word를 성공적으로 삭제한다")
        void deleteWord_Success() {
            // given
            Word word = new Word();
            word.setVoca(voca);

            when(wordRepository.findById(anyLong())).thenReturn(Optional.of(word));
            when(wordRepository.delete(anyLong())).thenReturn(word);

            // when
            Word result = wordService.deleteWord(1L, user.getId());

            // then
            assertNotNull(result);
            verify(wordRepository).delete(anyLong());
        }

        @Test
        @DisplayName("다른 사용자의 Word를 삭제하려고 하면 예외가 발생한다")
        void deleteWord_NotYourWord() {
            // given
            Word word = new Word();
            word.setVoca(voca);

            when(wordRepository.findById(anyLong())).thenReturn(Optional.of(word));

            // when & then
            assertThrows(NotYourWordException.class, 
                () -> wordService.deleteWord(1L, 999L));
        }
    }
} 