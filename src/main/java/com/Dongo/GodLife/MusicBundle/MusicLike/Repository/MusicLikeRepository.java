package com.Dongo.GodLife.MusicBundle.MusicLike.Repository;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicLikeRepository extends JpaRepository<MusicLike, Long> {
    
    List<MusicLike> findByUserId(Long userId);
    
    Optional<MusicLike> findByMusicIdAndUserId(Long musicId, Long userId);
    
    boolean existsByMusicIdAndUserId(Long musicId, Long userId);
    
    void deleteByMusicIdAndUserId(Long musicId, Long userId);
    
    @Query("SELECT ml FROM MusicLike ml WHERE ml.userId = :userId ORDER BY ml.id DESC")
    List<MusicLike> findByUserIdOrderByIdDesc(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT ml FROM MusicLike ml WHERE ml.userId = :userId AND ml.id < :cursorId ORDER BY ml.id DESC")
    List<MusicLike> findByUserIdAndIdLessThanOrderByIdDesc(
            @Param("userId") Long userId, 
            @Param("cursorId") Long cursorId, 
            Pageable pageable);
} 