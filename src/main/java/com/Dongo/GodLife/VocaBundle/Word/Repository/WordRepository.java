package com.Dongo.GodLife.VocaBundle.Word.Repository;

import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    @Query("SELECT w FROM Word w WHERE w.voca.vocaId = :vocaId ORDER BY w.wordId DESC")
    Page<Word> getAllWordsByVocaVocaId(@Param("vocaId") Long vocaId, Pageable pageable);

} 