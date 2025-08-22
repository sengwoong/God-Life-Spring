package com.Dongo.GodLife.MusicBundle.Music.Controller;

import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicRequest;
import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicResponse;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicService;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/musics")
@RequiredArgsConstructor
@Tag(name = "Music", description = "음악 관리 API")
public class MusicController {

    private final MusicService musicService;
    private final UserService userService;
    private final PlaylistService playlistService;

    @PostMapping("/playlist/{playlist_id}/user/{user_id}")
    @Operation(summary = "플레이리스트에 음악 추가", description = "사용자가 자신의 플레이리스트에 새로운 음악을 추가합니다.")
    public ResponseEntity<MusicResponse> createMusic(
            @Parameter(description = "플레이리스트 ID", example = "1")
            @PathVariable(name = "playlist_id") Long playlistId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid MusicRequest musicRequest) {
        userService.CheckUserAndGetUser(userId);
        Playlist playlist = playlistService.getPlayListById(playlistId);
        
        // 플레이리스트 소유자 체킹
        if (!playlist.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: User does not own the playlist");
        }
        
        Music createdMusic = musicService.createMusic(musicRequest, playlist);
        return ResponseEntity.ok(MusicResponse.from(createdMusic));
    }

        @GetMapping("/playlist/{playlist_id}/user/{user_id}")
    @Operation(summary = "플레이리스트 음악 조회", description = "사용자가 자신의 플레이리스트 음악 목록을 조회합니다.")
    public ResponseEntity<java.util.List<MusicResponse>> getMusicsByPlaylist(
            @Parameter(description = "플레이리스트 ID", example = "1")
            @PathVariable(name = "playlist_id") Long playlistId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "search", required = false) String search,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        userService.CheckUserAndGetUser(userId);
        Playlist playlist = playlistService.getPlayListById(playlistId);

        // 플레이리스트 소유자 체킹
        if (!playlist.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: User does not own the playlist");
        }

        Page<Music> musicPage = musicService.getAllMusicByPlaylist(playlistId, search, pageable);
        java.util.List<MusicResponse> responseList = musicPage.map(MusicResponse::from).getContent();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{music_id}/user/{user_id}")
    @Operation(summary = "음악 상세 조회", description = "사용자가 자신의 음악 상세 정보를 조회합니다.")
    public ResponseEntity<MusicResponse> getMusicById(
            @Parameter(description = "음악 ID", example = "1")
            @PathVariable(name = "music_id") Long musicId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable(name = "user_id") Long userId) {
        userService.CheckUserAndGetUser(userId);
        Music music = musicService.getMusicById(musicId);
        
        // 음악이 속한 플레이리스트의 소유자 체킹
        if (!music.getPlaylist().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: User does not own the music");
        }
        
        return ResponseEntity.ok(MusicResponse.from(music));
    }

    @PutMapping("/music/{music_id}/user/{user_id}")
    @Operation(summary = "음악 정보 수정", description = "사용자가 자신의 음악 정보를 수정합니다.")
    public ResponseEntity<MusicResponse> updateMusic(
            @Parameter(description = "음악 ID", example = "1")
            @PathVariable(name = "music_id") Long musicId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid MusicRequest musicRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Music updatedMusic = musicService.updateMusic(musicId, user, musicRequest);
        return ResponseEntity.ok(MusicResponse.from(updatedMusic));
    }

    @DeleteMapping("/music/{music_id}/user/{user_id}")
    @Operation(summary = "음악 삭제", description = "사용자가 자신의 음악을 삭제합니다.")
    public ResponseEntity<Void> deleteMusic(
            @Parameter(description = "음악 ID", example = "1")
            @PathVariable(name = "music_id") Long musicId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        musicService.deleteMusic(musicId, user);
        return ResponseEntity.noContent().build();
    }
} 