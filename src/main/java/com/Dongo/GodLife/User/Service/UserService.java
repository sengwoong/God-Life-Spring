package com.Dongo.GodLife.User.Service;


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


}
