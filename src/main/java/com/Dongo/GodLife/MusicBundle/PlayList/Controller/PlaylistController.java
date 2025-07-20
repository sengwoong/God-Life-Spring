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


    @GetMapping("/playlist/{playlist_id}/user/{user_id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable(name = "playlist_id") Long playlistId, @PathVariable(name = "user_id") Long userId) throws NotYourPlaylistException {
        User user = userService.CheckUserAndGetUser(userId);
        Playlist playlist = playlistService.getPlayListById(playlistId);
        if(!playlist.getUser().getId().equals(userId)){
            throw new NotYourPlaylistException("Access denied: User does not own the playlist");
        }
        return ResponseEntity.ok(playlist);
    }

    // @GetMapping("/playlist/{playlist_id}/share")
    // public ResponseEntity<Playlist> getPlaylistShareById(@PathVariable(name = "playlist_id") Long playlistId){
    //     // 공유 풀레이 리스트 단일일 조회
    // }

    // //유저의 공유 플레이리스트 전체조회
    // @GetMapping("/share/user/{user_id}")
    // public ResponseEntity<Page<Playlist>> getPlaylistShare(@PathVariable(name = "user_id") Long userId, Pageable pageable){
    //    // 유저의 공유 플레이리스트 전체조회
    // }

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
        playlistService.deletePlayList(playlistId, userId);
        return ResponseEntity.noContent().build();
    }
}
