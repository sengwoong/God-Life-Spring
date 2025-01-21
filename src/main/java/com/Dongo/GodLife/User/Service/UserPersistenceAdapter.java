package com.Dongo.GodLife.User.Service;

import com.Dongo.GodLife.User.Model.User;

import java.util.Optional;

public interface UserPersistenceAdapter {

    User save(User userRequest);

    Optional<User> findById(Long id);

}