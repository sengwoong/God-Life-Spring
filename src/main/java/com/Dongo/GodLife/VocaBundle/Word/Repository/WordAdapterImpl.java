package com.Dongo.GodLife.VocaBundle.Word.Repository;

import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.VocaBundle.Voca.Repository.VocaRepository;
import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import com.Dongo.GodLife.VocaBundle.Word.Service.WordPersistenceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WordAdapterImpl implements WordPersistenceAdapter {

    private final WordRepository wordRepository;

    @Autowired
    public WordAdapterImpl(WordRepository wordRepository ) {
        this.wordRepository = wordRepository;
    }

    @Override
    public Word save(Word wordRequest) {

        Word word = new Word();
        word.setWord(wordRequest.getWord());
        word.setMeaning(wordRequest.getMeaning());

        return wordRepository.save(word);
    }

    @Override
    public Page<Word> getAllWordsByVocaId(long vocaId, Pageable pageable) {
        return wordRepository.getAllWordsByVocaVocaId(vocaId, pageable);
    }

    @Override
    public Optional<Word> findById(long wordId) {
        return wordRepository.findById(wordId);
    }
    }
