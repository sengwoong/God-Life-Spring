package com.Dongo.GodLife.MusicBundle.Music.Controller;

import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicRequest;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicService;
import com.Dongo.GodLife.MusicBundle.PlayList.Exception.NotYourPlaylistException;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/musics")
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;
    private final UserService userService;
    private final PlaylistService playlistService;

    @PostMapping("/playlist/{playlist_id}/user/{user_id}")
    public ResponseEntity<Music> createMusic(
        @PathVariable(name = "playlist_id") Long playlistId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody MusicRequest musicRequest) throws NotYourPlaylistException {
        User user = userService.CheckUserAndGetUser(userId);      
        Playlist playlist = playlistService.getById(playlistId);
        if (!playlist.getUser().getId().equals(user.getId())) {
            throw new NotYourPlaylistException("Access denied: User does not own the playlist");
        }
        Music createdMusic = musicService.createMusic(musicRequest);
        
        return ResponseEntity.ok(createdMusic);
    }

    @GetMapping("/playlist/{playlist_id}")
    public ResponseEntity<Page<Music>> getPlaylistsByUserId(
            @PathVariable(name = "playlist_id") Long playlistId,
            Pageable pageable) {
        Page<Music> music = musicService.getAllMusicByPlaylist(playlistId, pageable);
        return ResponseEntity.ok(music);
    }

    @PutMapping("/music/{music_id}/user/{user_id}")
    public ResponseEntity<Music> updateMusic(
            @PathVariable(name = "user_id") Long userId,
            @PathVariable(name = "music_id") Long musicId,
            @RequestBody MusicRequest musicRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Music updatedMusic = musicService.updateMusic(musicId, user, musicRequest);
        return ResponseEntity.ok(updatedMusic);
    }

    @DeleteMapping("/music/{music_id}/user/{user_id}")
    public ResponseEntity<Void> deleteMusic(
            @PathVariable(name = "user_id") Long userId,
            @PathVariable(name = "music_id") Long musicId) {
        User user = userService.CheckUserAndGetUser(userId);
        musicService.deleteMusic(musicId, user);
        return ResponseEntity.noContent().build();
    }
} 