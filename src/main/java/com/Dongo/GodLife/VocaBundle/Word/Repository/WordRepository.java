package com.Dongo.GodLife.VocaBundle.Word.Repository;

import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    @Query("SELECT w FROM Word w WHERE w.voca.vocaId = :vocaId")
    Page<Word> getAllWordsByVocaVocaId(@Param("vocaId") long vocaId, Pageable pageable);

} 