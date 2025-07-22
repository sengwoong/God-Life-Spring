package com.Dongo.GodLife.VocaBundle.Word.Controller;



import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.Dongo.GodLife.VocaBundle.Word.Dto.WordRequest;
import com.Dongo.GodLife.VocaBundle.Word.Exception.NotYourWordException;
import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import com.Dongo.GodLife.VocaBundle.Word.Service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Word> createWord(@RequestBody WordRequest wordRequest) {
        Word createdWord = wordService.saveWord(wordRequest);
        return ResponseEntity.ok(createdWord);
    }

    @GetMapping("/voca/{voca_id}")
    public ResponseEntity<Page<Word>> getWordsByVocaId(
            @PathVariable(name = "voca_id") Long vocaId,
            Pageable pageable) {
        Page<Word> words = wordService.getAllwordsByVocaId(vocaId, pageable);
        return ResponseEntity.ok(words);
    }

    @PutMapping("/word/{word_id}/user/{user_id}")
    public ResponseEntity<Word> updateWord(
            @PathVariable(name = "user_id") Long userId,
            @PathVariable(name = "word_id") Long wordId,
            @RequestBody WordRequest wordRequest) throws NotYourWordException {
        User user = userService.CheckUserAndGetUser(userId);
        Word updatedWord = wordService.updateWord(wordId, wordRequest, user);
        return ResponseEntity.ok(updatedWord);
    }

    @DeleteMapping("/word/{word_id}/user/{user_id}")
    public ResponseEntity<Void> deleteWord(
            @PathVariable(name = "user_id") Long userId,
            @PathVariable(name = "word_id") Long wordId) throws NotYourWordException {
        User user = userService.CheckUserAndGetUser(userId);
        wordService.deleteWord(wordId, user);
        return ResponseEntity.noContent().build();
    }
}
