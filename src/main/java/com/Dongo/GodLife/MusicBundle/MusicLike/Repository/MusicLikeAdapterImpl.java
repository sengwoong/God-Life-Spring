package com.Dongo.GodLife.MusicBundle.MusicLike.Repository;

import com.Dongo.GodLife.MusicBundle.MusicLike.Model.MusicLike;
import com.Dongo.GodLife.MusicBundle.MusicLike.Service.MusicLikePersistenceAdapter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MusicLikeAdapterImpl implements MusicLikePersistenceAdapter {

    private final MusicLikeRepository musicLikeRepository;

    public MusicLikeAdapterImpl(MusicLikeRepository musicLikeRepository) {
        this.musicLikeRepository = musicLikeRepository;
    }

    @Override
    public List<MusicLike> findByUserId(Long userId) {
        return musicLikeRepository.findByUserId(userId);
    }

    @Override
    public Optional<MusicLike> findByMusicIdAndUserId(Long musicId, Long userId) {
        return musicLikeRepository.findByMusicIdAndUserId(musicId, userId);
    }

    @Override
    public boolean existsByMusicIdAndUserId(Long musicId, Long userId) {
        return musicLikeRepository.existsByMusicIdAndUserId(musicId, userId);
    }

    @Override
    public MusicLike save(MusicLike musicLike) {
        return musicLikeRepository.save(musicLike);
    }

    @Override
    public void deleteById(Long id) {
        musicLikeRepository.deleteById(id);
    }

    @Override
    public void deleteByMusicIdAndUserId(Long musicId, Long userId) {
        musicLikeRepository.deleteByMusicIdAndUserId(musicId, userId);
    }

    @Override
    public List<MusicLike> findByUserIdOrderByIdDesc(Long userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return musicLikeRepository.findByUserIdOrderByIdDesc(userId, pageable);
    }

    @Override
    public List<MusicLike> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long id, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return musicLikeRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, id, pageable);
    }
} 