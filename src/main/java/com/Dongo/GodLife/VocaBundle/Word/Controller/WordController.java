package com.Dongo.GodLife.VocaBundle.Word.Controller;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.JwtService;
import com.Dongo.GodLife.User.Service.UserService;
import com.Dongo.GodLife.VocaBundle.Word.Dto.WordRequest;
import com.Dongo.GodLife.VocaBundle.Word.Exception.NotYourWordException;
import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import com.Dongo.GodLife.VocaBundle.Word.Service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;
    private final UserService userService;
    private final JwtService jwtService; // reserved for future JWT validation usage

    @PostMapping
    public ResponseEntity<Word> createWord(@RequestBody @Valid WordRequest wordRequest) {
        Word createdWord = wordService.saveWord(wordRequest);
        return ResponseEntity.ok(createdWord);
    }

    @GetMapping("/word/{word_id}")
    public ResponseEntity<Word> getWordById(@PathVariable(name = "word_id") Long wordId) {
        Word word = wordService.findById(wordId);
        return ResponseEntity.ok(word);
    }

    @GetMapping("/voca/{voca_id}")
    public ResponseEntity<java.util.List<Word>> getWordsByVocaId(
            @PathVariable(name = "voca_id") Long vocaId,
            Pageable pageable) {
        // JWT 토큰에서 사용자 정보 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail);
        
        Page<Word> words = wordService.getAllwordsByVocaId(vocaId, user, pageable);
        return ResponseEntity.ok(words.getContent());
    }

    @PutMapping("/word/{word_id}/user/{user_id}")
    public ResponseEntity<Word> updateWord(
            @PathVariable(name = "word_id") Long wordId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid WordRequest wordRequest) throws NotYourWordException {
        User user = userService.CheckUserAndGetUser(userId);
        Word updatedWord = wordService.updateWord(wordId, wordRequest, user);
        return ResponseEntity.ok(updatedWord);
    }

    @DeleteMapping("/word/{word_id}/user/{user_id}")
    public ResponseEntity<Void> deleteWord(
            @PathVariable(name = "word_id") Long wordId,
            @PathVariable(name = "user_id") Long userId) throws NotYourWordException {
        User user = userService.CheckUserAndGetUser(userId);
        wordService.deleteWord(wordId, user);
        return ResponseEntity.noContent().build();
    }
}
