package com.Dongo.GodLife.VocaBundle;

import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.VocaBundle.Voca.Service.VocaPersistenceAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class VocaPersistenceAdapterStub implements VocaPersistenceAdapter {

    private final List<Voca> vocaList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Voca save(Voca voca) {
        if (voca.getVocaId() == null) {
            voca.setVocaId(idGenerator.getAndIncrement());
            vocaList.add(voca);
        } else {
            vocaList.removeIf(existingVoca -> existingVoca.getVocaId().equals(voca.getVocaId()));
            vocaList.add(voca);
        }
        return voca;
    }

    @Override
    public Page<Voca> findByUser(User user, Pageable pageable) {
        List<Voca> userVocas = new ArrayList<>();
        for (Voca voca : vocaList) {
            if (voca.getUser().equals(user)) {
                userVocas.add(voca);
            }
        }
        return new PageImpl<>(userVocas, pageable, userVocas.size());
    }

    @Override
    public Optional<Voca> findById(long id) {
        return vocaList.stream()
                .filter(voca -> voca.getVocaId() == id)
                .findFirst();
    }

    @Override
    public Voca delete(Voca voca) {
        vocaList.removeIf(existingVoca -> existingVoca.getVocaId().equals(voca.getVocaId()));
        return voca;
    }

    
    public User getUser(long vocaId) {
        return findById(vocaId).map(Voca::getUser).orElse(null);
    }
}
