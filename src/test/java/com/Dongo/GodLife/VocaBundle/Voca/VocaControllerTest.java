package com.Dongo.GodLife.VocaBundle.Voca;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.Dongo.GodLife.VocaBundle.Voca.Controller.VocaController;
import com.Dongo.GodLife.VocaBundle.Voca.Dto.VocaRequest;
import com.Dongo.GodLife.VocaBundle.Voca.Exception.NotYourVocaException;
import com.Dongo.GodLife.VocaBundle.Voca.Exception.VocaNotFoundException;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.VocaBundle.Voca.Service.VocaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VocaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VocaService vocaService;

    @Mock
    private UserService userService;

    @InjectMocks
    private VocaController vocaController;

    private ObjectMapper objectMapper;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@test.com");
        user1.setPassword("password");
        user1.setNickName("user1");
        user1.setLevel(1);
        user1.setFollowers(0);
        user1.setFollowing(0);
        user1.setSales(0);
        user1.setCreatedAt(LocalDateTime.now());
        
        user2 = new User();
        user2.setId(2L);
        user2.setEmail("test2@test.com");
        user2.setPassword("password");
        user2.setNickName("user2");
        user2.setLevel(1);
        user2.setFollowers(0);
        user2.setFollowing(0);
        user2.setSales(0);
        user2.setCreatedAt(LocalDateTime.now());

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        this.objectMapper = mapper;
        
        // MockMvc 설정
        mockMvc = MockMvcBuilders.standaloneSetup(vocaController)
                .setCustomArgumentResolvers(new org.springframework.data.web.PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(mapper))
                .build();
    }

    @Nested
    @DisplayName("Voca 관리")
    class VocaManagement {
        @Nested
        @DisplayName("Voca 생성")
        class CreateVoca {
            @Test
            @DisplayName("정상적인 요청일 경우, Voca를 생성한다")
            void createVoca_Success() throws Exception {
                // given
                VocaRequest vocaRequest = new VocaRequest();
                vocaRequest.setVocaTitle("world");
                vocaRequest.setLanguages("EN");
                vocaRequest.setDescription("세계");

                Voca voca = new Voca();
                voca.setVocaTitle("world");
                voca.setLanguages("EN");
                voca.setDescription("세계");

                // Spring Security 컨텍스트 Mock 설정
                Authentication authentication = mock(Authentication.class);
                SecurityContext securityContext = mock(SecurityContext.class);
                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getName()).thenReturn("test1@test.com");
                
                try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
                    mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
                    
                    when(userService.findByEmail("test1@test.com")).thenReturn(user1);
                    when(vocaService.createVoca(any(User.class), any(VocaRequest.class))).thenReturn(voca);

                    // when & then
                    mockMvc.perform(post("/vocas")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(vocaRequest)))
                            .andExpect(status().isOk());

                    verify(userService).findByEmail("test1@test.com");
                    verify(vocaService).createVoca(any(User.class), any(VocaRequest.class));
                }
            }
        }

        @DisplayName("Voca 조회")
        @Nested
        class GetVocas {
            @Test
            @DisplayName("사용자의 Voca 목록을 페이징하여 조회한다")
            void getVocasByUserId_Success() throws Exception {
                // given
                Voca voca = new Voca();
                voca.setVocaTitle("world");
                voca.setLanguages("EN");
                voca.setDescription("세계");

                // 실제 Pageable 객체 생성
                Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
                Page<Voca> vocaPage = new PageImpl<>(new java.util.ArrayList<>(), pageable, 0);

                // Spring Security 컨텍스트 Mock 설정
                Authentication authentication = mock(Authentication.class);
                SecurityContext securityContext = mock(SecurityContext.class);
                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getName()).thenReturn("test1@test.com");
                
                try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
                    mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
                    
                    when(userService.findByEmail("test1@test.com")).thenReturn(user1);
                    when(vocaService.getAllVocasByUserId(any(User.class), any(Pageable.class))).thenReturn(vocaPage);

                    // when & then
                    mockMvc.perform(get("/vocas/user")
                                    .with(request -> {
                                        request.setUserPrincipal(authentication);
                                        return request;
                                    }))
                            .andExpect(status().isOk());

                    verify(userService).findByEmail("test1@test.com");
                    verify(vocaService).getAllVocasByUserId(any(User.class), any(Pageable.class));
                }
            }
        }

        @DisplayName("Voca 수정")
        @Nested
        class UpdateVoca {
            @Test
            @DisplayName("정상적인 요청일 경우, 수정된 Voca를 반환한다")
            void updateVoca_Success() throws Exception, NotYourVocaException, VocaNotFoundException {
                // given
                VocaRequest vocaRequest = new VocaRequest();
                vocaRequest.setVocaTitle("updated");
                vocaRequest.setLanguages("EN");
                vocaRequest.setDescription("수정됨");

                Voca updatedVoca = new Voca();
                updatedVoca.setVocaTitle("updated");
                updatedVoca.setLanguages("EN");
                updatedVoca.setDescription("수정됨");

                when(userService.CheckUserAndGetUser(1L)).thenReturn(user1);
                when(vocaService.updateVoca(eq(1L), eq(user1), any(VocaRequest.class))).thenReturn(updatedVoca);

                // when & then
                mockMvc.perform(put("/vocas/voca/{voca_id}/user/{user_id}", 1L, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(vocaRequest)))
                        .andExpect(status().isOk());

                verify(userService).CheckUserAndGetUser(1L);
                verify(vocaService).updateVoca(eq(1L), eq(user1), any(VocaRequest.class));
            }
        }

        @DisplayName("Voca 삭제")
        @Nested
        class DeleteVoca {
            @Test
            @DisplayName("정상적인 요청일 경우, 204 status를 반환한다")
            void deleteVoca_Success() throws Exception, NotYourVocaException {
                // given
                when(userService.CheckUserAndGetUser(1L)).thenReturn(user1);
                doNothing().when(vocaService).deleteVoca(eq(1L), eq(user1));

                // when & then
                mockMvc.perform(delete("/vocas/voca/{voca_id}/user/{user_id}", 1L, 1L))
                        .andExpect(status().isNoContent());

                verify(userService).CheckUserAndGetUser(1L);
                verify(vocaService).deleteVoca(eq(1L), eq(user1));
            }
        }
    }
}