package com.Dongo.GodLife.MusicBundle.PlayList.Controller;


import com.Dongo.GodLife.MusicBundle.PlayList.Dto.PlaylistRequest;
import com.Dongo.GodLife.MusicBundle.PlayList.Exception.NotYourPlaylistException;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;
    private final UserService userService;

    @PostMapping("/user/{user_id}")
    public ResponseEntity<Playlist> createPlaylist(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody PlaylistRequest playlistRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Playlist playlist = playlistService.createPlaylist(playlistRequest, user);
        return ResponseEntity.ok(playlist);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<Playlist>> getPlaylistsByUserId(
            @PathVariable(name = "user_id") Long userId,
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Playlist> playlists = playlistService.getAllPlaylistsByUserId(user, pageable);
        return ResponseEntity.ok(playlists);
    }

    @PutMapping("/playlist/{playlist_id}/user/{user_id}")
    public ResponseEntity<Playlist> updatePlaylist(
            @PathVariable(name = "playlist_id") Long playlistId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody PlaylistRequest playlistRequest) throws NotYourPlaylistException {
        User user = userService.CheckUserAndGetUser(userId);
        Playlist updatedPlaylist = playlistService.updatePlayList(playlistId, userId, playlistRequest);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/playlist/{playlist_id}/user/{user_id}")
    public ResponseEntity<Void> deletePlaylist(
            @PathVariable(name = "playlist_id") Long playlistId,
            @PathVariable(name = "user_id") Long userId) throws NotYourPlaylistException {
        userService.CheckUserAndGetUser(userId);
        playlistService.deletePlaylist(playlistId, userId);
        return ResponseEntity.noContent().build();
    }
}
