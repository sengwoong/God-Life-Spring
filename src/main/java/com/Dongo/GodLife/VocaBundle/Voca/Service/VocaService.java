package com.Dongo.GodLife.VocaBundle.Voca.Service;

import com.Dongo.GodLife.User.Service.UserPersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.Dongo.GodLife.VocaBundle.Voca.Dto.VocaRequest;
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



}
