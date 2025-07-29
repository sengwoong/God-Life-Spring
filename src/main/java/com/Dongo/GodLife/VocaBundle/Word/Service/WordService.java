package com.Dongo.GodLife.VocaBundle.Word.Service;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.VocaBundle.Voca.Service.VocaPersistenceAdapter;
import com.Dongo.GodLife.VocaBundle.Word.Dto.WordRequest;
import com.Dongo.GodLife.VocaBundle.Word.Exception.NotYourWordException;
import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.Validator;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordPersistenceAdapter wordRepository;
    private final VocaPersistenceAdapter vocaRepository;

    public Word saveWord(WordRequest wordRequest) {
        Voca voca = vocaRepository.findById(wordRequest.getVocaId())
                .orElseThrow(() -> new EntityNotFoundException("단어장을 찾을 수 없습니다."));

        Word word = new Word();
        word.setWord(wordRequest.getWord());
        word.setMeaning(wordRequest.getMeaning());
        word.setVoca(voca);
        return wordRepository.save(word);
    }

    public Page<Word> getAllwordsByVocaId(Long vocaId, User user, Pageable pageable) {
        Voca voca = vocaRepository.findById(vocaId)
                .orElseThrow(() -> new EntityNotFoundException("Voca not found with id: " + vocaId));
        if(voca.getUser().getId().equals(user.getId())){
            return wordRepository.getAllWordsByVocaId(vocaId, pageable);
        }
        throw new NotYourWordException("Access denied: User does not own the word");
    }

    public Word findById(Long wordId) {
        return wordRepository.findById(wordId)
                .orElseThrow(() -> new EntityNotFoundException("Word not found with id: " + wordId));
    }

    public Word updateWord(Long wordId, WordRequest wordRequest, User user) throws NotYourWordException {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new EntityNotFoundException("Word not found with id: " + wordId));

        if(!word.getVoca().getUser().getId().equals(user.getId())){
            throw new NotYourWordException("Access denied: User does not own the word");
        }

        word.setWord(wordRequest.getWord());
        word.setMeaning(wordRequest.getMeaning());

        return wordRepository.save(word);
    }

    public Word deleteWord(Long wordId, User user) throws NotYourWordException {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new EntityNotFoundException("Word not found with id: " + wordId));

        if( !word.getVoca().getUser().getId().equals(user.getId())){
            throw new NotYourWordException("Access denied: User does not own the word");
        }
        wordRepository.delete(wordId);

        return word;
    }
}
