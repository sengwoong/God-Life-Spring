package com.Dongo.GodLife.MusicBundle.PlayList.Repository;


import com.Dongo.GodLife.MusicBundle.PlayList.Exception.NotYourPlaylistException;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistPersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlaylistAdapterImpl implements PlaylistPersistenceAdapter {

    private final PlaylistRepository playlistRepository;

    public PlaylistAdapterImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    public Playlist save(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    @Override
    public Page<Playlist> findByUser(User user, Pageable pageable) {
        return playlistRepository.findByUser(user, pageable);
    }
    
    @Override
    public Page<Playlist> findByUserWithSearch(User user, String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return playlistRepository.findByUser(user, pageable);
        }
        return playlistRepository.findByUserAndPlaylistTitleContaining(user, search.trim(), pageable);
    }

    @Override
    public Optional<Playlist> findById(Long playlistId) {
        return playlistRepository.findById(playlistId);
    }

    @Override
    public Page<Playlist> findByIsShared(Boolean isShared, Pageable pageable) {
        return playlistRepository.findByIsShared(isShared, pageable);
    }

    @Override
    public Playlist delete(Playlist playlist){
        playlistRepository.delete(playlist);
        return playlist;
    }

    @Override
    public Page<Playlist> findByUserAndIsShared(User user, Boolean isShared, Pageable pageable) {
        return playlistRepository.findByUserAndIsShared(user, isShared, pageable);
    }
}
