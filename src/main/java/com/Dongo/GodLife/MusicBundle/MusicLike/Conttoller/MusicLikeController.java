package com.Dongo.GodLife.MusicBundle.MusicLike.Conttoller;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import com.Dongo.GodLife.MusicBundle.MusicLike.Service.MusicLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musiclikes")
@RequiredArgsConstructor
public class MusicLikeController {

    private final MusicLikeService musicLikeService;

   


    @DeleteMapping("/{musicId}/user/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Long musicId, @PathVariable Long userId) {
        musicLikeService.removeLike(musicId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{musicId}/user/{userId}/check")
    public ResponseEntity<Boolean> isLiked(@PathVariable Long musicId, @PathVariable Long userId) {
        boolean isLiked = musicLikeService.isLiked(musicId, userId);
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/musics/liked/{user_id}")
    public ResponseEntity<Page<MusicLike>> getLikedMusicsByUserIdWithPagination(
            @PathVariable Long userId, 
            Pageable pageable) {
        Page<MusicLike> likedMusics = musicLikeService.getLikedMusicsByUserIdWithPagination(userId, pageable);
        return ResponseEntity.ok(likedMusics);
    
            }
}
