package com.Dongo.GodLife.User.Service;

import com.Dongo.GodLife.User.Model.User;

import java.util.Optional;

public interface UserPersistenceAdapter {

    User save(User userRequest);

    Optional<User> findById(Long id);

    void delete(Long id);
    
    // Follow 관련 메서드 추가
    User saveUser(User user);
    
    Optional<User> findByEmail(String email);
}