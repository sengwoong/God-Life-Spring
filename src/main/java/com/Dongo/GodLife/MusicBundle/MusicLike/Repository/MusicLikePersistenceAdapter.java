package com.Dongo.GodLife.MusicBundle.MusicLike.Repository;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicLikePersistenceAdapter extends JpaRepository<MusicLike, Long> {
    
    List<MusicLike> findByUserId(Long userId);
    
    Optional<MusicLike> findByMusicIdAndUserId(Long musicId, Long userId);
    
    boolean existsByMusicIdAndUserId(Long musicId, Long userId);
    
    Page<MusicLike> findByUserIdOrderByMusicLikeIdDesc(Long userId, Pageable pageable);
} 