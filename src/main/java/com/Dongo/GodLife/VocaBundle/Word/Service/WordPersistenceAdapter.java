package com.Dongo.GodLife.VocaBundle.Word.Service;


import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WordPersistenceAdapter {


    Word save(Word wordRequest);

    Page<Word> getAllWordsByVocaId(long vocaId, Pageable pageable);

}
