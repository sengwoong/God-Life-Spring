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
            @PathVariable long user_id,
            @RequestBody PlaylistRequest playlistRequest) {
        User user =userService.CheckUserAndGetUser(user_id);
        Playlist playlist = playlistService.createPlaylist(playlistRequest,user);
        return ResponseEntity.ok(playlist);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<Playlist>> getPlaylistsByUserId(
            @PathVariable long user_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User user =userService.CheckUserAndGetUser(user_id);
        Pageable pageable = PageRequest.of(page, size);
        Page<Playlist> playlists = playlistService.getAllPlaylistsByUserId( user,pageable);
        return ResponseEntity.ok(playlists);
    }

    @PutMapping("/playlist/{playlist_id}/user/{user_id}")
    public ResponseEntity<Playlist> updatePlaylist(
            @PathVariable long playlist_id,
            @PathVariable long user_id,
            @RequestBody PlaylistRequest playlistRequest) throws NotYourPlaylistException {
        User user = userService.CheckUserAndGetUser(user_id);
        Playlist updatedPlaylist = playlistService.updatePlayList(playlist_id,user_id, playlistRequest);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/playlist/{playlist_id}/user/{user_id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable long playlist_id, @PathVariable long user_id) throws NotYourPlaylistException {
        userService.CheckUserAndGetUser(user_id);
        playlistService.deletePlaylist(playlist_id, user_id);
        return ResponseEntity.noContent().build();
    }
}
