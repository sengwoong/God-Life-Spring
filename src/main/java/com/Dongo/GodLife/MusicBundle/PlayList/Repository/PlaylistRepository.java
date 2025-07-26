package com.Dongo.GodLife.MusicBundle.PlayList.Repository;


import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Page<Playlist> findByUser(User user, Pageable pageable);  

    Page<Playlist> findByIsShared(Boolean isShared, Pageable pageable);
    
    // 사용자별 공유 플레이리스트 조회
    Page<Playlist> findByUserAndIsShared(User user, Boolean isShared, Pageable pageable);
    
    // 검색 기능 추가
    @Query("SELECT p FROM Playlist p WHERE p.user = :user AND p.playlistTitle LIKE %:search%")
    Page<Playlist> findByUserAndPlaylistTitleContaining(
        @Param("user") User user, 
        @Param("search") String search, 
        Pageable pageable
    );
}