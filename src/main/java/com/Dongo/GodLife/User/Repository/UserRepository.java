package com.Dongo.GodLife.User.Repository;

import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long Id);
    Optional<User> findByEmail(String email);
}
