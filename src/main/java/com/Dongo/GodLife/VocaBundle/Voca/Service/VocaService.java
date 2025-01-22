package com.Dongo.GodLife.VocaBundle.Voca.Service;

import com.Dongo.GodLife.User.Service.UserPersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.Dongo.GodLife.VocaBundle.Voca.Dto.VocaRequest;
import com.Dongo.GodLife.VocaBundle.Voca.Exception.NotYourVocaException;
import com.Dongo.GodLife.VocaBundle.Voca.Exception.VocaNotFoundException;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.Validator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VocaService {
    private final VocaPersistenceAdapter vocaRepository;

    public Voca createVoca(User user, @Valid VocaRequest vocaRequest) {

        Voca voca = new Voca();
        Validator.validateNotEmpty(vocaRequest.getVocaTitle(), "Voca title cannot be empty");
        Validator.validateNotEmpty(vocaRequest.getDescription(), "Voca description cannot be empty");
        voca.setDescription(vocaRequest.getDescription());
        voca.setVocaTitle(vocaRequest.getVocaTitle());
        voca.setUser(user);
        return vocaRepository.save(voca);
    }

    public Page<Voca> getAllVocasByUserId(User user, Pageable pageable) {
        return vocaRepository.findByUser(user, pageable);
    }

    public Voca updateVoca(long vocaId,User user, VocaRequest vocaRequest) throws NotYourVocaException, VocaNotFoundException {

        Optional<Voca> optionalVoca = vocaRepository.findById(vocaId);
        if (!optionalVoca.isPresent()) {
            throw new VocaNotFoundException("Voca not found");
        }
        Voca voca = optionalVoca.get();

        Validator.validateNotEmpty(vocaRequest.getVocaTitle(), "Voca title cannot be empty");
        Validator.validateNotEmpty(vocaRequest.getDescription(), "Voca description cannot be empty");

        if (!voca.getUser().getId().equals(user.getId())) {
            throw new NotYourVocaException("Access denied: User does not own the voca");
        }

        voca.setDescription(vocaRequest.getDescription());
        voca.setVocaTitle(vocaRequest.getVocaTitle());
        voca.setUser(user);
        return vocaRepository.save(voca);
    }

    public void deleteVoca(long vocaId,long userId) throws NotYourVocaException {
        Optional<Voca> voca = vocaRepository.findById(vocaId);
        if(!voca.get().getUser().getId().equals(userId)){
            throw new NotYourVocaException("Access denied: User does not own the playlist");
        }

        vocaRepository.delete(voca.orElse(null));
    }
}
