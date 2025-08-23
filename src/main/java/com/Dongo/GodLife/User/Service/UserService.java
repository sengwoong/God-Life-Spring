package com.Dongo.GodLife.User.Service;


import com.Dongo.GodLife.User.Dto.UpdateUserRequest;
import com.Dongo.GodLife.User.Dto.UserResponse;
import com.Dongo.GodLife.User.Exception.UserNotFoundException;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import util.Validator;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserPersistenceAdapter userPersistenceAdapter;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String email, String password, String nickName) {
        Validator.validateNickName(nickName);
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
        user.setNickName(nickName);
        user.setCreatedAt(LocalDateTime.now());
        return userPersistenceAdapter.save(user);
    }

    public User CheckUserAndGetUser(Long Id) {
        return userPersistenceAdapter.findById(Id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + Id));
    }

    public UserResponse getUserByUserDetail(Long Id) {
        User user = userPersistenceAdapter.findById(Id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + Id));
        return new UserResponse(user);
    }

    public UpdateUserRequest updateUser(Long Id, UpdateUserRequest request) {
        User user = userPersistenceAdapter.findById(Id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + Id));

        user.setNickName(request.getNickName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());

        User updatedUser = userPersistenceAdapter.save(user);
        return new UpdateUserRequest(updatedUser);
    }

    public void deleteUser(Long Id) {
        User user = CheckUserAndGetUser(Id);
        userPersistenceAdapter.delete(Id);
    }
    

    public User saveUser(User user) {
        return userPersistenceAdapter.saveUser(user);
    }

    public User findByEmail(String email) {
        return userPersistenceAdapter.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public User authenticateUser(String email, String password) {
        User user = findByEmail(email);
        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    public User findById(Long id) {
        return userPersistenceAdapter.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}
