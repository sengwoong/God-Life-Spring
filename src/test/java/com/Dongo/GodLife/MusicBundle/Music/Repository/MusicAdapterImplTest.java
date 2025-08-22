package com.Dongo.GodLife.MusicBundle.Music.Repository;

import com.Dongo.GodLife.MusicBundle.Music.Exception.NotYourMusicException;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
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
class MusicAdapterImplTest {

    @Mock
    private MusicRepository musicRepository;

    @InjectMocks
    private MusicAdapterImpl musicAdapter;

    @Test
    @DisplayName("save/findById 위임")
    void saveAndFindById() {
        Music m = new Music();
        m.setMusicId(1L);
        given(musicRepository.save(m)).willReturn(m);
        given(musicRepository.findById(1L)).willReturn(Optional.of(m));

        assertEquals(1L, musicAdapter.save(m).getMusicId());
        assertTrue(musicAdapter.findById(1L).isPresent());
        verify(musicRepository).save(m);
        verify(musicRepository).findById(1L);
    }

    @Test
    @DisplayName("findPlaylistMusics/WithSearch 위임 및 분기")
    void pageQueries() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Music> page = new PageImpl<>(List.of(new Music()));

        given(musicRepository.findAllByPlaylist_PlaylistIdOrderByCreatedAtDesc(5L, pageable)).willReturn(page);
        given(musicRepository.findAllByPlaylistIdAndMusicTitleContaining(5L, "abc", pageable)).willReturn(page);

        assertEquals(1, musicAdapter.findPlaylistMusics(5L, pageable).getTotalElements());
        assertEquals(1, musicAdapter.findPlaylistMusicsWithSearch(5L, null, pageable).getTotalElements());
        assertEquals(1, musicAdapter.findPlaylistMusicsWithSearch(5L, "   ", pageable).getTotalElements());
        assertEquals(1, musicAdapter.findPlaylistMusicsWithSearch(5L, "abc", pageable).getTotalElements());
    }

    @Test
    @DisplayName("delete: null이면 예외, 있으면 삭제")
    void deleteBehavior() throws NotYourMusicException {
        assertThrows(NotYourMusicException.class, () -> musicAdapter.delete(null));

        Music m = new Music();
        m.setMusicId(9L);
        Music deleted = musicAdapter.delete(m);
        assertEquals(9L, deleted.getMusicId());
        verify(musicRepository).delete(m);
    }
}


