package com.Dongo.GodLife.User.Controller;


import com.Dongo.GodLife.User.Dto.UpdateUserRequest;
import com.Dongo.GodLife.User.Dto.UserCreateRequest;
import com.Dongo.GodLife.User.Dto.UserResponse;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(userService.createUser(request.getEmail(), request.getPassword(),request.getNickName()));
    }

    @GetMapping("user/{user_id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long user_id) {
        return ResponseEntity.ok(userService.getUserByUserDetail(user_id));
    }

    @PutMapping("user/{user_id}")
    public ResponseEntity<UpdateUserRequest> updateUser(@PathVariable long user_id, @RequestBody UpdateUserRequest user) {
        return ResponseEntity.ok(userService.updateUser(user_id, user));
    }


}
