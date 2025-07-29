package com.Dongo.GodLife.MusicBundle.MusicLike.Repository;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicLikePersistenceAdapter extends JpaRepository<MusicLike, Long> {
    
    @Query("SELECT ml FROM MusicLike ml WHERE ml.user.id = :userId")
    List<MusicLike> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ml FROM MusicLike ml WHERE ml.music.musicId = :musicId AND ml.user.id = :userId")
    Optional<MusicLike> findByMusicIdAndUserId(@Param("musicId") Long musicId, @Param("userId") Long userId);
    
    @Query("SELECT CASE WHEN COUNT(ml) > 0 THEN true ELSE false END FROM MusicLike ml WHERE ml.music.musicId = :musicId AND ml.user.id = :userId")
    boolean existsByMusicIdAndUserId(@Param("musicId") Long musicId, @Param("userId") Long userId);
    
    @Query("SELECT ml FROM MusicLike ml WHERE ml.user.id = :userId ORDER BY ml.musicLikeId DESC")
    Page<MusicLike> findByUserIdOrderByMusicLikeIdDesc(@Param("userId") Long userId, Pageable pageable);
} 