package com.Dongo.GodLife.MusicBundle.MusicLike.Conttoller;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import com.Dongo.GodLife.MusicBundle.MusicLike.Service.MusicLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/musiclikes")
@RequiredArgsConstructor
@Tag(name = "MusicLike", description = "음악 좋아요 관리 API")
public class MusicLikeController {

    private final MusicLikeService musicLikeService;

    @PostMapping("/{musicId}/user/{userId}")
    @Operation(summary = "음악 좋아요 토글", description = "사용자가 음악에 좋아요를 추가하거나 제거합니다.")
    public ResponseEntity<Void> toggleLike(
            @Parameter(description = "음악 ID", example = "1")
            @PathVariable(name = "musicId") Long musicId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable(name = "userId") Long userId) {
        musicLikeService.toggleLike(musicId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/music/{musicId}/user/{userId}/check")
    @Operation(summary = "음악 좋아요 상태 확인", description = "사용자가 특정 음악에 좋아요를 눌렀는지 확인합니다.")
    public ResponseEntity<Boolean> isLiked(
            @Parameter(description = "음악 ID", example = "1")
            @PathVariable(name = "musicId") Long musicId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable(name = "userId") Long userId) {
        boolean isLiked = musicLikeService.isLiked(musicId, userId);
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/user/{userId}/liked")
    @Operation(summary = "사용자 좋아요 음악 목록 조회", description = "사용자가 좋아요한 음악 목록을 페이지네이션과 함께 조회합니다.")
    public ResponseEntity<Page<MusicLike>> getLikedMusicsByUserIdWithPagination(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable(name = "userId") Long userId, 
            @Parameter(description = "페이지네이션 파라미터")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false) String sort) {
        
        Pageable pageable = PageRequest.of(page, size);
        if (sort != null && !sort.trim().isEmpty()) {
            String[] sortParts = sort.split(",");
            if (sortParts.length >= 2) {
                String property = sortParts[0];
                String direction = sortParts[1];
                Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? 
                    Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(page, size, Sort.by(sortDirection, property));
            }
        }
        
        Page<MusicLike> likedMusics = musicLikeService.getLikedMusicsByUserIdWithPagination(userId, pageable);
        return ResponseEntity.ok(likedMusics);
    }
}
