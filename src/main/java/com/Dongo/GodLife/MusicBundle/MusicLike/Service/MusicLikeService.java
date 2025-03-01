package com.Dongo.GodLife.MusicBundle.MusicLike.Service;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicLikeService {
    
    private final MusicLikePersistenceAdapter musicLikeAdapter;
    
    public List<MusicLike> getLikedMusicsByUserId(Long userId) {
        return musicLikeAdapter.findByUserId(userId);
    }
    
    @Transactional
    public MusicLike addLike(Long musicId, Long userId) {

        if (musicLikeAdapter.existsByMusicIdAndUserId(musicId, userId)) {
            throw new IllegalStateException("이미 좋아요한 음악입니다.");
        }
              
        MusicLike musicLike = MusicLike.builder()
                .musicId(musicId)
                .userId(userId)
                .build();
                
        return musicLikeAdapter.save(musicLike);
    }
    
    @Transactional
    public void removeLike(Long musicId, Long userId) {
        Optional<MusicLike> musicLike = musicLikeAdapter.findByMusicIdAndUserId(musicId, userId);
        
        if (musicLike.isPresent()) {
            musicLikeAdapter.deleteById(musicLike.get().getId());
        } else {
            throw new IllegalStateException("좋아요가 존재하지 않습니다.");
        }
    }
    
    public boolean isLiked(Long musicId, Long userId) {
        return musicLikeAdapter.existsByMusicIdAndUserId(musicId, userId);
    }

    public List<MusicLike> getLikedMusicsByUserIdWithCursor(Long userId, Long cursor, int limit) {
        
        if (cursor == null) {
            return musicLikeAdapter.findByUserIdOrderByIdDesc(userId, limit);
        } else {
            return musicLikeAdapter.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, limit);
        }
    }
} 