package com.Dongo.GodLife.VocaBundle.Voca;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.VocaBundle.Voca.Service.VocaService;
import com.Dongo.GodLife.VocaBundle.Voca.Dto.VocaRequest;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.VocaBundle.Voca.Exception.NotYourVocaException;
import com.Dongo.GodLife.VocaBundle.Voca.Exception.VocaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
class VocaServiceTests {

    @InjectMocks
    VocaService sut;

    @Spy
    VocaPersistenceAdapterStub vocaRepository;

    @Mock
    User user;
    

    User user1;
    User user2;

    @BeforeEach
    void setUpUser() {
        user1 = new User("test@test.com");
        user1.setId(1L);
        user2 = new User("test2@test.com");
        user2.setId(2L);
    }

    @Nested
    class CreateVoca {
        @DisplayName("새로운 Voca를 생성하고 저장한다")
        @Test
        void testCreateVoca() {
            // given
            VocaRequest vocaRequest = new VocaRequest();
            vocaRequest.setVocaTitle("world");
            vocaRequest.setDescription("세계");

            // when
            final Voca result = sut.createVoca(user1, vocaRequest);

            // then
            assertNotNull(result);
            assertNotNull(result.getVocaId());
            assertEquals("world", result.getVocaTitle());
            assertEquals("세계", result.getDescription());
        }
    }

    @Nested
    class GetAllVocasByUserId {
        @DisplayName("사용자의 모든 Voca를 페이지 단위로 가져온다")
        @Test
        void testGetAllVocasByUserId_Empty() {
            // given
            Pageable pageable = mock(Pageable.class);
            when(vocaRepository.findByUser(user1, pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));

            // when
            Page<Voca> result = sut.getAllVocasByUserId(user1, pageable);

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @DisplayName("사용자의 Voca가 하나 있을 때 가져온다")
        @Test
        void testGetAllVocasByUserId_Single() {
            // given
            Pageable pageable = mock(Pageable.class);
            Voca voca = new Voca();
            voca.setUser(user1);
            when(vocaRepository.findByUser(user1, pageable)).thenReturn(new PageImpl<>(Collections.singletonList(voca)));

            // when
            Page<Voca> result = sut.getAllVocasByUserId(user1, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }

        @DisplayName("사용자의 Voca가 여러 개 있을 때 가져온다")
        @Test
        void testGetAllVocasByUserId_Multiple() {
            // given
            Pageable pageable = mock(Pageable.class);
            Voca voca1 = new Voca();
            Voca voca2 = new Voca();
            voca1.setUser(user1);
            voca2.setUser(user1);
            when(vocaRepository.findByUser(user1, pageable)).thenReturn(new PageImpl<>(Arrays.asList(voca1, voca2)));

            // when
            Page<Voca> result = sut.getAllVocasByUserId(user1, pageable);

            // then
            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
        }
    }
    @Nested
    class UpdateVoca {
        @DisplayName("자신의 Voca를 업데이트한다")
        @Test
        void testUpdateOwnVoca() throws NotYourVocaException, VocaNotFoundException {
            // given
            Voca existingVoca = new Voca();
            existingVoca.setVocaId(1L);
            existingVoca.setUser(user1);
            when(vocaRepository.findById(1L)).thenReturn(Optional.of(existingVoca));
          
            VocaRequest vocaRequest = new VocaRequest();
            vocaRequest.setVocaTitle("updated title");
            vocaRequest.setDescription("updated description");
    
            // when
            Voca result = sut.updateVoca(1L, user1, vocaRequest);
    
            // then
            assertNotNull(result);
            assertEquals("updated title", result.getVocaTitle());
            assertEquals("updated description", result.getDescription());
        }
    
    

        @DisplayName("다른 사용자의 Voca를 업데이트하려고 하면 예외가 발생한다")
        @Test
        void testUpdateOthersVoca() {
            // given
            Voca existingVoca = new Voca();
            existingVoca.setVocaId(1L);
            existingVoca.setUser(user2);
            when(vocaRepository.findById(1L)).thenReturn(Optional.of(existingVoca));


            VocaRequest vocaRequest = new VocaRequest();
            vocaRequest.setVocaTitle("updated title");
            vocaRequest.setDescription("updated description");

            // when & then
            assertThrows(NotYourVocaException.class, () -> sut.updateVoca(1L, user1, vocaRequest));
        }

        @DisplayName("존재하지 않는 Voca를 업데이트하려고 하면 예외가 발생한다")
        @Test
        void testUpdateNonExistingVoca() {
            // given
            VocaRequest vocaRequest = new VocaRequest();
            vocaRequest.setVocaTitle("updated title");
            vocaRequest.setDescription("updated description");

            // when & then
            assertThrows(VocaNotFoundException.class, () -> sut.updateVoca(1L, user1, vocaRequest));
        }
    }

    @Nested
    class DeleteVoca {
        @DisplayName("자신의 Voca를 삭제한다")
        @Test
        void testDeleteOwnVoca() throws NotYourVocaException {
            // given
            Voca existingVoca = new Voca();
            existingVoca.setVocaId(1L);
            existingVoca.setUser(user1);
            when(vocaRepository.findById(1L)).thenReturn(Optional.of(existingVoca));

            // when
            sut.deleteVoca(1L, user1.getId());

            // then
            verify(vocaRepository).delete(existingVoca);
        }

        @DisplayName("다른 사용자의 Voca를 삭제하려고 하면 예외가 발생한다")
        @Test
        void testDeleteOthersVoca() {
            // given
            Voca existingVoca = new Voca();
            existingVoca.setVocaId(1L);

            existingVoca.setUser(user2);
            when(vocaRepository.findById(1L)).thenReturn(Optional.of(existingVoca));

            // when & then
            assertThrows(NotYourVocaException.class, () -> sut.deleteVoca(1L, user1.getId()));
        }

        @DisplayName("존재하지 않는 Voca를 삭제하려고 하면 예외가 발생한다")
        @Test
        void testDeleteNonExistingVoca() {
            // when & then
            assertThrows(NoSuchElementException.class, () -> sut.deleteVoca(30L, user1.getId()));
        }
    }

    @Nested
    class FindById {
        final Long existingVocaId = 1L;
        final String word = "hello";
        final String meaning = "안녕하세요";

        @BeforeEach
        void setUp() {
            Voca voca = new Voca();
            voca.setVocaId(existingVocaId);
            voca.setVocaTitle(word);
            voca.setDescription(meaning);
            voca.setUser(user1);
            vocaRepository.save(voca);
        }

        @DisplayName("ID로 Voca를 찾지 못하면, null을 반환한다")
        @Test
        void test1() {
            // given
            final Long nonExistingVocaId = 2L;

            // when
            final Voca result = sut.findById(nonExistingVocaId);

            // then
            assertNull(result);
        }

        @DisplayName("ID로 Voca를 찾으면, Voca를 반환한다")
        @Test
        void test1000() {
            // when
            final Voca result = sut.findById(existingVocaId);

            // then
            assertNotNull(result);
            assertEquals(existingVocaId, result.getVocaId());
            assertEquals(word, result.getVocaTitle());
            assertEquals(meaning, result.getDescription());
            assertEquals(user1, result.getUser());
        }
    }
} 