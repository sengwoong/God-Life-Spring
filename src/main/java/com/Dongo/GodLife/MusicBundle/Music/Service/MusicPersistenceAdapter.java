package com.Dongo.GodLife.MusicBundle.Music.Service;

import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MusicPersistenceAdapter {

    Music save(Music music);

    Page<Music> findPlaylistMusics(long musicId, Pageable pageable);

    Optional<Music> findById(long musicId);

}

