package com.Dongo.GodLife.VocaBundle.Voca.Controller;


import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.Dongo.GodLife.VocaBundle.Voca.Dto.VocaRequest;
import com.Dongo.GodLife.VocaBundle.Voca.Exception.NotYourVocaException;
import com.Dongo.GodLife.VocaBundle.Voca.Exception.VocaNotFoundException;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.VocaBundle.Voca.Service.VocaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vocas")
@RequiredArgsConstructor
public class VocaController {
    private final VocaService vocaService;
    private final UserService userService;

    @PostMapping("/user/{user_id}")
    public ResponseEntity<Voca> createVoca(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid VocaRequest vocaRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Voca voca = vocaService.createVoca(user, vocaRequest);
        return ResponseEntity.ok(voca);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<Voca>> getVocasByUserId(
            @PathVariable(name = "user_id") Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Voca> vocas = vocaService.getAllVocasByUserId(user, pageable);
        return ResponseEntity.ok(vocas);
    }

    @GetMapping("/voca/{voca_id}")
    public ResponseEntity<Voca> getVocaById(@PathVariable(name = "voca_id") Long vocaId) {
        Voca voca = vocaService.findById(vocaId);
        return ResponseEntity.ok(voca);
    }

    @GetMapping("/share/{user_id}")
    public ResponseEntity<Page<Voca>> shareVoca(
            @PathVariable(name = "user_id") Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Voca> vocas = vocaService.getSharedVocasByUserId(user, pageable);
        return ResponseEntity.ok(vocas);
    }

    @GetMapping("/purchased/{user_id}")
    public ResponseEntity<Page<Voca>> getPurchasedVocasByUserId(
            @PathVariable(name = "user_id") Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Voca> vocas = vocaService.getPurchasedVocasByUserId(user, pageable);
        return ResponseEntity.ok(vocas);
    }

    @GetMapping("/study/{user_id}")
    public ResponseEntity<Page<Voca>> getStudyVocasByUserId(
            @PathVariable(name = "user_id") Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Voca> vocas = vocaService.getStudyVocasByUserId(user, pageable);
        return ResponseEntity.ok(vocas);
    }


    @PutMapping("/voca/{voca_id}/user/{user_id}")
    public ResponseEntity<Voca> updateVoca(
            @PathVariable(name = "voca_id") Long vocaId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid VocaRequest vocaRequest) throws NotYourVocaException, VocaNotFoundException {
        User user = userService.CheckUserAndGetUser(userId);
        Voca updatedVoca = vocaService.updateVoca(vocaId, user, vocaRequest);
        return ResponseEntity.ok(updatedVoca);
    }

    @PutMapping("/share/{voca_id}/user/{user_id}")
    public ResponseEntity<Voca> shareVoca(
            @PathVariable(name = "voca_id") Long vocaId,
            @PathVariable(name = "user_id") Long userId
            ) throws NotYourVocaException, VocaNotFoundException {
        User user = userService.CheckUserAndGetUser(userId);
        Voca updatedVoca = vocaService.shareUpdateVoca(vocaId, user);
        return ResponseEntity.ok(updatedVoca);
    }

    @DeleteMapping("/voca/{voca_id}/user/{user_id}")
    public ResponseEntity<Void> deleteVoca(
            @PathVariable(name = "voca_id") Long vocaId,
            @PathVariable(name = "user_id") Long userId) throws NotYourVocaException {
        User user = userService.CheckUserAndGetUser(userId);
        vocaService.deleteVoca(vocaId, user);
        return ResponseEntity.noContent().build();
    }
}
