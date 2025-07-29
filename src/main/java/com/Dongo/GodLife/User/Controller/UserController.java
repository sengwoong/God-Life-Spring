package com.Dongo.GodLife.User.Controller;

import com.Dongo.GodLife.User.Dto.FollowResponse;
import com.Dongo.GodLife.User.Dto.UpdateUserRequest;
import com.Dongo.GodLife.User.Dto.UserCreateRequest;
import com.Dongo.GodLife.User.Dto.UserResponse;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.FollowService;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity.ok(userService.createUser(
            request.getEmail(), 
            request.getPassword(),
            request.getNickName()));
    }

    @GetMapping("user/{user_id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable(name = "user_id") Long user_id) {
        return ResponseEntity.ok(userService.getUserByUserDetail(user_id));
    }

    @PutMapping("user/{user_id}")
    public ResponseEntity<UpdateUserRequest> updateUser(
            @PathVariable(name = "user_id") Long user_id,
            @RequestBody @Valid UpdateUserRequest user) {
        return ResponseEntity.ok(userService.updateUser(user_id, user));
    }

    @DeleteMapping("user/{user_id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable(name = "user_id") Long user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.noContent().build();
    }
    
    
    @PostMapping("/{follower_id}/follow/{following_id}")
    public ResponseEntity<FollowResponse> toggleFollow(
            @PathVariable(name = "follower_id") Long followerId,
            @PathVariable(name = "following_id") Long followingId) {
        FollowResponse response = followService.toggleFollow(followerId, followingId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{follower_id}/follow/{following_id}")
    public ResponseEntity<Map<String, Object>> checkFollowStatus(
            @PathVariable(name = "follower_id") Long followerId,
            @PathVariable(name = "following_id") Long followingId) {
        boolean isFollowing = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(Map.of(
            "followerId", followerId,
            "followingId", followingId,
            "isFollowing", isFollowing
        ));
    }
    
    @GetMapping("/{user_id}/followers")
    public ResponseEntity<Page<UserResponse>> getFollowers(
            @PathVariable(name = "user_id") Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<User> followers = followService.getFollowers(userId, pageable);
        Page<UserResponse> response = followers.map(UserResponse::new);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{user_id}/following")
    public ResponseEntity<Page<UserResponse>> getFollowing(
            @PathVariable(name = "user_id") Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<User> following = followService.getFollowing(userId, pageable);
        Page<UserResponse> response = following.map(UserResponse::new);
        return ResponseEntity.ok(response);
    }
}
