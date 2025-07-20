package com.Dongo.GodLife.MusicBundle.MusicLike.Service;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import com.Dongo.GodLife.MusicBundle.MusicLike.Repository.MusicLikePersistenceAdapter;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicLikeService {
    
    private final MusicLikePersistenceAdapter musicLikeAdapter;
    

    
    public MusicLike addLike(Long musicId, Long userId) {

        if (musicLikeAdapter.existsByMusicIdAndUserId(musicId, userId)) {
            return null;
        }
              
        MusicLike musicLike = new MusicLike();
        Music music = new Music();
        music.setMusicId(musicId);
        User user = new User();
        user.setId(userId);
        musicLike.setMusic(music);
        musicLike.setUser(user);
                
        return musicLikeAdapter.save(musicLike);
    }
    
    public void removeLike(Long musicId, Long userId) {
        Optional<MusicLike> musicLike = musicLikeAdapter.findByMusicIdAndUserId(musicId, userId);
        
        if (musicLike.isPresent()) {
            musicLikeAdapter.deleteById(musicLike.get().getMusicLikeId());
        } 
    }
    
    public boolean isLiked(Long musicId, Long userId) {
        return musicLikeAdapter.existsByMusicIdAndUserId(musicId, userId);
    }

    public Page<MusicLike> getLikedMusicsByUserIdWithPagination(Long userId, Pageable pageable) {
        return musicLikeAdapter.findByUserIdOrderByMusicLikeIdDesc(userId, pageable);
    }
} 