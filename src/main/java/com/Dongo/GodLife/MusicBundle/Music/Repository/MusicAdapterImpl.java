package com.Dongo.GodLife.MusicBundle.Music.Repository;



import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicPersistenceAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MusicAdapterImpl implements MusicPersistenceAdapter {

    private final MusicRepository musicRepository;

    public MusicAdapterImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @Override
    public Music save(Music music) {
        return musicRepository.save(music);
    }

    @Override
    public Page<Music> findPlaylistMusics(long musicId, Pageable pageable) {
        return musicRepository.findAllByPlaylist_PlaylistId(musicId,pageable);
    }

    @Override
    public Optional<Music> findById(long musicId) {
        return musicRepository.findById(musicId);
    }

}
