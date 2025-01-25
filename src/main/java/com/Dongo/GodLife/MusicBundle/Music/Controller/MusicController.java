package com.Dongo.GodLife.MusicBundle.Music.Controller;


import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicRequest;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/musics")
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;
    private final UserService userService;

    @PostMapping
    public Music createMusic(@RequestBody MusicRequest musicRequest) {
        return musicService.createMusic(musicRequest);
    }

    @GetMapping("/playlist/{playlist_id}")
    public ResponseEntity<Page<Music>> getPlaylistsByUserId(
            @PathVariable Long playlist_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Music> music = musicService.getAllMusicByPlaylist(playlist_id, pageable);
        return ResponseEntity.ok(music);
    }

    @PutMapping("/music/{music_id}/user/{user_id}")
    public ResponseEntity<Music> updateMusic(@PathVariable long user_id,@PathVariable Long music_id, @RequestBody MusicRequest musicDetails) {
        User user= userService.CheckUserAndGetUser(user_id);
        Music updatedMusic = musicService.updateMusic(music_id,user,musicDetails);
        return ResponseEntity.ok(updatedMusic);
    }

    @DeleteMapping("/music/{music_id}/user/{user_id}")
    public ResponseEntity<Void> deleteMusic(@PathVariable long music_id,@PathVariable long user_id) {
        User user= userService.CheckUserAndGetUser(user_id);
        musicService.deleteMusic(music_id,user);
        return ResponseEntity.noContent().build();
    }
} 