package com.Dongo.GodLife.MusicBundle.Music.Repository;


import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MusicRepository extends JpaRepository<Music, Long> {
    Page<Music> findAllByPlaylist_PlaylistId(Long playlistId, Pageable pageable);
}

