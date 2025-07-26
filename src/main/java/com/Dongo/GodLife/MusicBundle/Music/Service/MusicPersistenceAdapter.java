package com.Dongo.GodLife.MusicBundle.Music.Service;

import com.Dongo.GodLife.MusicBundle.Music.Exception.NotYourMusicException;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MusicPersistenceAdapter {

    Music save(Music music);

    Page<Music> findPlaylistMusics(Long musicId, Pageable pageable);
    
    // 검색 기능 추가
    Page<Music> findPlaylistMusicsWithSearch(Long playlistId, String search, Pageable pageable);

    Optional<Music> findById(Long musicId);

    Music delete(Music music) throws NotYourMusicException;

}

