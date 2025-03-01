package com.Dongo.GodLife.MusicBundle.MusicLike.Controller;

import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicService;
import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import com.Dongo.GodLife.MusicBundle.MusicLike.Service.MusicLikeService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/musicLikes")
@RequiredArgsConstructor
public class MusicLikeController {
    
    private final MusicLikeService musicLikeService;
    private final MusicService musicService;
    private final UserService userService;
    
    @GetMapping("/user/{user_id}")
    public ResponseEntity<Map<String, Object>> getLikedMusics(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "arg1", required = false) Long cursor,
            @RequestParam(name = "arg2", defaultValue = "10") int limit) {
        try {
            User user = userService.CheckUserAndGetUser(userId);
            
            List<MusicLike> likedMusics = musicLikeService.getLikedMusicsByUserIdWithCursor(userId, cursor, limit + 1);
            System.out.println(likedMusics);
            boolean hasNext = false;
            if (likedMusics.size() > limit) {
                hasNext = true;
                likedMusics.remove(likedMusics.size() - 1);
            }
            
            List<Map<String, Object>> musicDetailsList = new ArrayList<>();
            Long nextCursor = null;
    
            for (MusicLike like : likedMusics) {
                System.out.println(like);
                Music music = musicService.getMusicById(like.getMusicId());
                System.out.println(music);
                if (music != null) {
                    Map<String, Object> musicDetails = new HashMap<>();
                    musicDetails.put("musicId", music.getMusicId());
                    musicDetails.put("musicTitle", music.getMusicTitle());
                    musicDetails.put("musicUrl", music.getMusicUrl());
                    musicDetails.put("imageUrl", music.getImageUrl());
                    musicDetails.put("color", music.getColor());
                    
                    musicDetailsList.add(musicDetails);
                    nextCursor = like.getId();
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            
            Map<String, Object> data = new HashMap<>();
            data.put("like", musicDetailsList);
            
            Map<String, Object> pagination = new HashMap<>();
            pagination.put("hasNext", hasNext);
            if (hasNext) {
                pagination.put("nextCursor", nextCursor);
            }
            data.put("pagination", pagination);
            
            response.put("data", data);
            response.put("message", "좋아요한 음악 목록을 성공적으로 조회했습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    @PostMapping("/music/{music_id}/user/{user_id}")
    public ResponseEntity<MusicLike> addLike(
            @PathVariable(name = "music_id") Long musicId,
            @PathVariable(name = "user_id") Long userId) {
        
        User user = userService.CheckUserAndGetUser(userId);
        Music music = musicService.getMusicById(musicId);
        
        MusicLike musicLike = musicLikeService.addLike(musicId, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(musicLike);
    }
    
    @DeleteMapping("/music/{music_id}/user/{user_id}")
    public ResponseEntity<Void> removeLike(
            @PathVariable(name = "music_id") Long musicId,
            @PathVariable(name = "user_id") Long userId) {
        
        User user = userService.CheckUserAndGetUser(userId);
        
        musicLikeService.removeLike(musicId, userId);
        
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/check/music/{music_id}/user/{user_id}")
    public ResponseEntity<Boolean> isLiked(
            @PathVariable(name = "music_id") Long musicId,
            @PathVariable(name = "user_id") Long userId) {
        
        boolean isLiked = musicLikeService.isLiked(musicId, userId);
        
        return ResponseEntity.ok(isLiked);
    }
} 