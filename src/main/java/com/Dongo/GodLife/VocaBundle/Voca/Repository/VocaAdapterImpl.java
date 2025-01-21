package com.Dongo.GodLife.VocaBundle.Voca.Repository;


import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.VocaBundle.Voca.Service.VocaPersistenceAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VocaAdapterImpl implements VocaPersistenceAdapter {

    private final VocaRepository vocaRepository;

    public VocaAdapterImpl(VocaRepository vocaRepository) {
        this.vocaRepository = vocaRepository;
    }

    @Override
    public Voca save(Voca voca) {
        return vocaRepository.save(voca);
    }
    @Override
    public  Page<Voca> findByUser(User user, Pageable pageable) {
        return vocaRepository.findByUser(user, pageable);
    }

}
