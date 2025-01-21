package com.Dongo.GodLife.User.Repository;



import com.Dongo.GodLife.User.Exception.UserNotFoundException;
import com.Dongo.GodLife.User.Service.UserPersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAdapterImpl implements UserPersistenceAdapter {

    private final UserRepository userRepository;

    @Autowired
    public UserAdapterImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 저장
    @Override
    public User save(User userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setNickName(userRequest.getNickName());
        user.setCreatedAt(userRequest.getCreatedAt());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        userRepository.delete(user);
    }
}