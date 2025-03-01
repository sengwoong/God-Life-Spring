package com.Dongo.GodLife.MusicBundle.MusicLike.Service;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;

import java.util.List;
import java.util.Optional;

public interface MusicLikePersistenceAdapter {

    List<MusicLike> findByUserId(Long userId);
    
    Optional<MusicLike> findByMusicIdAndUserId(Long musicId, Long userId);
    
    boolean existsByMusicIdAndUserId(Long musicId, Long userId);
    
    MusicLike save(MusicLike musicLike);
    
    void deleteById(Long id);
    
    void deleteByMusicIdAndUserId(Long musicId, Long userId);
    
    List<MusicLike> findByUserIdOrderByIdDesc(Long userId, int limit);
    
    List<MusicLike> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long id, int limit);
} 