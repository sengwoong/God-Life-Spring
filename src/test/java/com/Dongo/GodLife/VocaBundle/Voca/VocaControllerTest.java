package com.Dongo.GodLife.VocaBundle;

import com.Dongo.GodLife.VocaBundle.Voca.Controller.VocaController;
import com.Dongo.GodLife.VocaBundle.Voca.Dto.VocaRequest;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.VocaBundle.Voca.Service.VocaService;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.Dongo.GodLife.VocaBundle.Voca.Exception.NotYourVocaException;
import com.Dongo.GodLife.VocaBundle.Voca.Exception.VocaNotFoundException;

@WebMvcTest(VocaController.class)
class VocaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VocaService vocaService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Voca 관리")
    @Nested
    class VocaManagement {

        @DisplayName("Voca 생성")
        @Nested
        class CreateVoca {
            @Test
            @DisplayName("정상적인 요청일 경우, 200 status와 Voca를 반환한다")
            void createVoca_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                VocaRequest vocaRequest = new VocaRequest();
                vocaRequest.setVocaTitle("world");
                vocaRequest.setDescription("세계");

                Voca voca = new Voca();
                voca.setVocaTitle("world");
                voca.setDescription("세계");

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(vocaService.createVoca(any(User.class), any(VocaRequest.class))).willReturn(voca);

                // when & then
                mockMvc.perform(post("/vocas/user/{user_id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(vocaRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.vocaTitle").value("world"))
                        .andExpect(jsonPath("$.description").value("세계"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(vocaService).createVoca(any(User.class), any(VocaRequest.class));
            }
        }

        @DisplayName("Voca 조회")
        @Nested
        class GetVocas {
            @Test
            @DisplayName("사용자의 Voca 목록을 페이징하여 조회한다")
            void getVocasByUserId_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                Voca voca = new Voca();
                voca.setVocaTitle("world");
                voca.setDescription("세계");

                PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
                Page<Voca> vocaPage = new PageImpl<>(List.of(voca));

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(vocaService.getAllVocasByUserId(any(User.class), any())).willReturn(vocaPage);

                // when & then
                mockMvc.perform(get("/vocas/user/{user_id}", 1L))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].vocaTitle").value("world"))
                        .andExpect(jsonPath("$.content[0].description").value("세계"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(vocaService).getAllVocasByUserId(any(User.class), any());
            }
        }

        @DisplayName("Voca 수정")
        @Nested
        class UpdateVoca {
            @Test
            @DisplayName("정상적인 요청일 경우, 수정된 Voca를 반환한다")
            void updateVoca_Success() throws Exception, NotYourVocaException, VocaNotFoundException {
                // given
                User user = new User("test@test.com");
                VocaRequest vocaRequest = new VocaRequest();
                vocaRequest.setVocaTitle("updated");
                vocaRequest.setDescription("수정됨");

                Voca updatedVoca = new Voca();
                updatedVoca.setVocaTitle("updated");
                updatedVoca.setDescription("수정됨");

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(vocaService.updateVoca(anyLong(), any(User.class), any(VocaRequest.class)))
                    .willReturn(updatedVoca)
                    .willThrow(NotYourVocaException.class)
                    .willThrow(VocaNotFoundException.class);

                // when & then
                mockMvc.perform(put("/vocas/voca/{voca_id}/user/{user_id}", 1L, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(vocaRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.vocaTitle").value("updated"))
                        .andExpect(jsonPath("$.description").value("수정됨"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(vocaService).updateVoca(anyLong(), any(User.class), any(VocaRequest.class));
            }
        }

        @DisplayName("Voca 삭제")
        @Nested
        class DeleteVoca {
            @Test
            @DisplayName("정상적인 요청일 경우, 204 status를 반환한다")
            void deleteVoca_Success() throws Exception, NotYourVocaException {
                // given
                doNothing()
                    .when(vocaService)
                    .deleteVoca(anyLong(), anyLong());

                // when & then
                mockMvc.perform(delete("/vocas/voca/{voca_id}/user/{user_id}", 1L, 1L))
                        .andExpect(status().isNoContent());

                verify(vocaService).deleteVoca(anyLong(), anyLong());
            }

    
        }
    }
}