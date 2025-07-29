package com.Dongo.GodLife.MusicBundle.MusicLike.Service;

import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Repository.MusicRepository;
import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import com.Dongo.GodLife.MusicBundle.MusicLike.Repository.MusicLikePersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MusicLikeService {
    private final MusicLikePersistenceAdapter musicLikeAdapter;
    private final UserService userService;
    private final MusicRepository musicRepository;

    @Transactional
    public MusicLike addLike(Long musicId, Long userId) {
        if (musicLikeAdapter.existsByMusicIdAndUserId(musicId, userId)) {
            return null;
        }
        
        User user = userService.CheckUserAndGetUser(userId);
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new IllegalArgumentException("Music not found with id: " + musicId));
        
        MusicLike musicLike = MusicLike.builder()
                .music(music)
                .user(user)
                .build();
        
        return musicLikeAdapter.save(musicLike);
    }

    @Transactional
    public void toggleLike(Long musicId, Long userId) {
        Optional<MusicLike> musicLike = musicLikeAdapter.findByMusicIdAndUserId(musicId, userId);
        if (musicLike.isPresent()) {
            musicLikeAdapter.deleteById(musicLike.get().getMusicLikeId());
        } else {
            addLike(musicId, userId);
        }
    }

    public boolean isLiked(Long musicId, Long userId) {
        return musicLikeAdapter.existsByMusicIdAndUserId(musicId, userId);
    }

    public Page<MusicLike> getLikedMusicsByUserIdWithPagination(Long userId, Pageable pageable) {
        return musicLikeAdapter.findByUserIdOrderByMusicLikeIdDesc(userId, pageable);
    }
} 