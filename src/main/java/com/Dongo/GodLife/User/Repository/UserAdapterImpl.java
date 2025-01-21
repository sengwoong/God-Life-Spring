package com.Dongo.GodLife.User.Repository;



import com.Dongo.GodLife.User.Service.UserPersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


}