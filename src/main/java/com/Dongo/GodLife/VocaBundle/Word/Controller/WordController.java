package com.Dongo.GodLife.VocaBundle.Word.Controller;



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
            @PathVariable Long voca_id,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Word> words = wordService.getAllwordsByVocaId(voca_id, pageable);
        return ResponseEntity.ok(words);
    }

    @PutMapping("/word/{word_id}/user/{user_id}")
    public ResponseEntity<Word> updateWord(
            @PathVariable Long user_id,
            @PathVariable Long word_id,
            @RequestBody WordRequest wordRequest) throws NotYourWordException {

        userService.CheckUserAndGetUser(user_id);
        Word updatedWord = wordService.updateWord(word_id,user_id,wordRequest);
        return ResponseEntity.ok(updatedWord);
    }

    @DeleteMapping("/word/{word_id}/user/{user_id}")
    public ResponseEntity<Word> deleteWord(
            @PathVariable Long user_id,
            @PathVariable Long word_id) throws NotYourWordException {

        userService.CheckUserAndGetUser(user_id);
        wordService.deleteWord(word_id,user_id);
        return ResponseEntity.noContent().build();
    }
}
