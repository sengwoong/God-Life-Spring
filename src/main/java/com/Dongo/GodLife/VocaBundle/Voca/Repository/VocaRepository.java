package com.Dongo.GodLife.VocaBundle.Voca.Repository;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocaRepository extends JpaRepository<Voca, Long> {
    Page<Voca> findByUser(User user, Pageable pageable);

    Page<Voca> findByUserAndIsShared(User user, Boolean isShared, Pageable pageable);
}
