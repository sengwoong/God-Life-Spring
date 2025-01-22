package com.Dongo.GodLife.VocaBundle.Voca.Service;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VocaPersistenceAdapter  {

    Voca save(Voca voca);

    @Nullable Page<Voca> findByUser(@NotNull User user, Pageable pageable);

    @Nullable Optional<Voca> findById(@NotNull long id);

    Voca delete(Voca voca);
}
