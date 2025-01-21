package com.Dongo.GodLife.User.Service;


import com.Dongo.GodLife.User.Dto.UpdateUserRequest;
import com.Dongo.GodLife.User.Dto.UserResponse;
import com.Dongo.GodLife.User.Exception.UserNotFoundException;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import util.Validator;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserPersistenceAdapter userRepository;

    public User createUser(String email, String password, String nickName) {
        Validator.validateNickName(nickName);
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setNickName(nickName);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User CheckUserAndGetUser(long Id) {
        return userRepository.findById(Id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + Id));
    }


    public UserResponse getUserByUserDetail(long Id) {
        User user = userRepository.findById(Id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + Id));
        return new UserResponse(user);
    }

    public UpdateUserRequest updateUser(long Id, UpdateUserRequest request) {
        User user = userRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + Id));

        user.setNickName(request.getNickName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);
        return new UpdateUserRequest(updatedUser);
    }


}
