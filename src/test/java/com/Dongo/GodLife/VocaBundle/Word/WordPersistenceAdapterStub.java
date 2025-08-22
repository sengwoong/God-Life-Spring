package com.Dongo.GodLife.VocaBundle.Word;

import com.Dongo.GodLife.VocaBundle.Word.Exception.NotYourWordException;
import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import com.Dongo.GodLife.VocaBundle.Word.Service.WordPersistenceAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class WordPersistenceAdapterStub implements WordPersistenceAdapter {

    private final List<Word> wordList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Word save(Word word) {
        if (word.getWordId() == null) {
            word.setWordId(idGenerator.getAndIncrement());
            wordList.add(word);
        } else {
            wordList.removeIf(existingWord -> existingWord.getWordId().equals(word.getWordId()));
            wordList.add(word);
        }
        return word;
    }

    @Override
    public Page<Word> getAllWordsByVocaId(Long vocaId, Pageable pageable) {
        List<Word> vocaWords = wordList.stream()
                .filter(word -> word.getVoca().getVocaId() == vocaId)
                .collect(Collectors.toList());
        return new PageImpl<>(vocaWords, pageable, vocaWords.size());
    }

    @Override
    public Optional<Word> findById(Long wordId) {
        return wordList.stream()
                .filter(word -> word.getWordId() == wordId)
                .findFirst();
    }

    @Override
    public Word delete(Long wordId) throws NotYourWordException {
        Optional<Word> wordOptional = findById(wordId);
        if (wordOptional.isEmpty()) {
            throw new NotYourWordException("Word not found");
        }
        Word word = wordOptional.get();
        wordList.removeIf(existingWord -> existingWord.getWordId() == wordId);
        return word;
    }
} 