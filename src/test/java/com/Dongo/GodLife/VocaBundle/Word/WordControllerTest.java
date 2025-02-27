package com.Dongo.GodLife.VocaBundle.Word;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.Dongo.GodLife.VocaBundle.Word.Controller.WordController;
import com.Dongo.GodLife.VocaBundle.Word.Dto.WordRequest;
import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import com.Dongo.GodLife.VocaBundle.Word.Service.WordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WordController.class)
class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordService wordService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Word 관리")
    @Nested
    class WordManagement {

        @DisplayName("Word 생성")
        @Nested
        class CreateWord {
            @Test
            @DisplayName("정상적인 요청일 경우, 200 status와 Word를 반환한다")
            void createWord_Success() throws Exception {
                // given
                WordRequest wordRequest = new WordRequest();
                wordRequest.setWord("apple");
                wordRequest.setMeaning("사과");
                wordRequest.setVocaId(1L);

                Word word = new Word();
                word.setWord("apple");
                word.setMeaning("사과");

                given(wordService.saveWord(any(WordRequest.class))).willReturn(word);

                // when & then
                mockMvc.perform(post("/words")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(wordRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.word").value("apple"))
                        .andExpect(jsonPath("$.meaning").value("사과"));

                verify(wordService).saveWord(any(WordRequest.class));
            }
        }

        @DisplayName("Word 조회")
        @Nested
        class GetWords {
            @Test
            @DisplayName("Voca의 Word 목록을 조회한다")
            void getWordsByVocaId_Success() throws Exception {
                // given
                Word word = new Word();
                word.setWord("apple");
                word.setMeaning("사과");

                Page<Word> wordPage = new PageImpl<>(List.of(word));

                given(wordService.getAllwordsByVocaId(anyLong(), any())).willReturn(wordPage);

                // when & then
                mockMvc.perform(get("/words/voca/{voca_id}", 1L))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].word").value("apple"))
                        .andExpect(jsonPath("$.content[0].meaning").value("사과"));

                verify(wordService).getAllwordsByVocaId(anyLong(), any());
            }
        }

        @DisplayName("Word 수정")
        @Nested
        class UpdateWord {
            @Test
            @DisplayName("정상적인 요청일 경우, 수정된 Word를 반환한다")
            void updateWord_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                WordRequest wordRequest = new WordRequest();
                wordRequest.setWord("updated");
                wordRequest.setMeaning("수정됨");

                Word updatedWord = new Word();
                updatedWord.setWord("updated");
                updatedWord.setMeaning("수정됨");

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(wordService.updateWord(anyLong(), anyLong(), any(WordRequest.class)))
                        .willReturn(updatedWord);

                // when & then
                mockMvc.perform(put("/words/word/{word_id}/user/{user_id}", 1L, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(wordRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.word").value("updated"))
                        .andExpect(jsonPath("$.meaning").value("수정됨"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(wordService).updateWord(anyLong(), anyLong(), any(WordRequest.class));
            }
        }

        @DisplayName("Word 삭제")
        @Nested
        class DeleteWord {
            @Test
            @DisplayName("정상적인 요청일 경우, 204 status를 반환한다")
            void deleteWord_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);

                // when & then
                mockMvc.perform(delete("/words/word/{word_id}/user/{user_id}", 1L, 1L))
                        .andExpect(status().isNoContent());

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(wordService).deleteWord(anyLong(), anyLong());
            }
        }
    }
} 