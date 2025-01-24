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
    public ResponseEntity<Voca> createVoca(@PathVariable long user_id, @RequestBody @Valid VocaRequest voca) {
        User user = userService.CheckUserAndGetUser(user_id);
        return ResponseEntity.ok(vocaService.createVoca(user, voca));
    }


    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<Voca>> getVocasUserId(
            @PathVariable long user_id,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable)  {
            User user = userService.CheckUserAndGetUser(user_id);
            Page<Voca> vocas = vocaService.getAllVocasByUserId(user, pageable);
            return ResponseEntity.ok(vocas);

    }

    @PutMapping("voca/{voca_id}/user/{user_id}")
    public ResponseEntity<Voca> updateUser(@PathVariable long voca_id, @PathVariable long user_id, @RequestBody @Valid VocaRequest voca) throws  NotYourVocaException, VocaNotFoundException {
        User user = userService.CheckUserAndGetUser(user_id);
        return ResponseEntity.ok(vocaService.updateVoca(voca_id, user, voca));
    }


    @DeleteMapping("voca/{voca_id}/user/{user_id}")
    public ResponseEntity<Void> deleteVoca(@PathVariable long voca_id,@PathVariable long user_id) throws NotYourVocaException {
        vocaService.deleteVoca(voca_id,user_id);
        return ResponseEntity.noContent().build();
    }
}
