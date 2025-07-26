package com.Dongo.GodLife.MusicBundle.Music.Repository;


import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MusicRepository extends JpaRepository<Music, Long> {
    Page<Music> findAllByPlaylist_PlaylistId(Long playlistId, Pageable pageable);
    
    // 검색 기능 추가
    @Query("SELECT m FROM Music m WHERE m.playlist.playlistId = :playlistId AND m.musicTitle LIKE %:search%")
    Page<Music> findAllByPlaylistIdAndMusicTitleContaining(
        @Param("playlistId") Long playlistId, 
        @Param("search") String search, 
        Pageable pageable
    );
}

