package com.Dongo.GodLife.MusicBundle.PlayList.Repository;

import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
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
class PlaylistAdapterImplTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private PlaylistAdapterImpl playlistAdapter;

    @Test
    @DisplayName("save/findById/delete 위임")
    void basicDelegations() {
        Playlist p = new Playlist();
        p.setPlaylistId(1L);
        given(playlistRepository.save(p)).willReturn(p);
        given(playlistRepository.findById(1L)).willReturn(Optional.of(p));

        assertEquals(1L, playlistAdapter.save(p).getPlaylistId());
        assertTrue(playlistAdapter.findById(1L).isPresent());

        Playlist deleted = playlistAdapter.delete(p);
        assertEquals(1L, deleted.getPlaylistId());
        verify(playlistRepository).delete(p);
    }

    @Test
    @DisplayName("페이지 조회 위임: findByUser/findByIsShared/findByUserWithSearch")
    void pageDelegations() {
        PageRequest pageable = PageRequest.of(0, 10);
        User user = User.builder().id(3L).build();
        Page<Playlist> page = new PageImpl<>(List.of(new Playlist()));

        given(playlistRepository.findByUser(user, pageable)).willReturn(page);
        given(playlistRepository.findByIsShared(true, pageable)).willReturn(page);
        given(playlistRepository.findByUserAndPlaylistTitleContaining(user, "abc", pageable)).willReturn(page);

        assertEquals(1, playlistAdapter.findByUser(user, pageable).getTotalElements());
        assertEquals(1, playlistAdapter.findByIsShared(true, pageable).getTotalElements());
        assertEquals(1, playlistAdapter.findByUserWithSearch(user, null, pageable).getTotalElements());
        assertEquals(1, playlistAdapter.findByUserWithSearch(user, "   ", pageable).getTotalElements());
        assertEquals(1, playlistAdapter.findByUserWithSearch(user, "abc", pageable).getTotalElements());
    }
}


