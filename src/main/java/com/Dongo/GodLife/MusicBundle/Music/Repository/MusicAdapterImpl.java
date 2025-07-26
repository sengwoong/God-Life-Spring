package com.Dongo.GodLife.MusicBundle.Music.Repository;



import com.Dongo.GodLife.MusicBundle.Music.Exception.NotYourMusicException;
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
    public Page<Music> findPlaylistMusics(Long musicId, Pageable pageable) {
        return musicRepository.findAllByPlaylist_PlaylistId(musicId,pageable);
    }
    
    @Override
    public Page<Music> findPlaylistMusicsWithSearch(Long playlistId, String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return musicRepository.findAllByPlaylist_PlaylistId(playlistId, pageable);
        }
        return musicRepository.findAllByPlaylistIdAndMusicTitleContaining(playlistId, search.trim(), pageable);
    }

    @Override
    public Optional<Music> findById(Long musicId) {
        return musicRepository.findById(musicId);
    }

    @Override
    public Music delete(Music music) throws NotYourMusicException {
        if (music == null) {
            throw new NotYourMusicException("Music cannot be null");
        }
        musicRepository.delete(music);
        return music;
    }

}
