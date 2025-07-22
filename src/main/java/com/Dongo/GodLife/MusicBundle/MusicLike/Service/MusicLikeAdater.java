package com.Dongo.GodLife.MusicBundle.MusicLike.Service;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MusicLikeAdater {
    MusicLike save(MusicLike musicLike);
    void deleteById(Long musicLikeId);
    boolean existsByMusicIdAndUserId(Long musicId, Long userId);
    Optional<MusicLike> findByMusicIdAndUserId(Long musicId, Long userId);
    Page<MusicLike> findByUserIdOrderByMusicLikeIdDesc(Long userId, Pageable pageable);

}
