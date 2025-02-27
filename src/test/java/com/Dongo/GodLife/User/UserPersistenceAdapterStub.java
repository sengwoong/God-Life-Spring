package com.Dongo.GodLife.User;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserPersistenceAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class UserPersistenceAdapterStub implements UserPersistenceAdapter {

    private final List<User> userList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
            userList.add(user);
        } else {
            userList.removeIf(existingUser -> existingUser.getId().equals(user.getId()));
            userList.add(user);
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public void delete(Long id) {
        userList.removeIf(user -> user.getId().equals(id));
    }
} 