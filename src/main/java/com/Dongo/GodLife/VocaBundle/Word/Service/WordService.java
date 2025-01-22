package com.Dongo.GodLife.VocaBundle.Word.Service;

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


        Validator.validateNotEmpty(wordRequest.getWord(), "Word cannot be empty");
        Validator.validateNotEmpty(wordRequest.getMeaning(), "Word meaning cannot be empty");

        Word word = new Word();
        word.setWord(wordRequest.getWord());
        word.setMeaning(wordRequest.getMeaning());
        word.setVoca(voca);
        return wordRepository.save(word);
    }

    public Page<Word> getAllwordsByVocaId(long vocaId, Pageable pageable) {
        return wordRepository.getAllWordsByVocaId(vocaId, pageable);
    }

    public Word updateWord(long wordId,long userId, WordRequest wordRequest) throws NotYourWordException {

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new EntityNotFoundException("Word not found with id: " + wordId));


        if(!word.getVoca().getUser().getId().equals(userId)){
            throw new NotYourWordException("Access denied: User does not own the word");
        }

        word.setWord(wordRequest.getWord());
        word.setMeaning(wordRequest.getMeaning());

        return wordRepository.save(word);
    }

    public Word deleteWord(long wordId, long userId) throws NotYourWordException {

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new EntityNotFoundException("Word not found with id: " + wordId));

        if( !word.getVoca().getUser().getId().equals(userId)){
            throw new NotYourWordException("Access denied: User does not own the word");
        }
        wordRepository.delete(wordId);

        return word;
    }

}
