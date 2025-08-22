package com.Dongo.GodLife.MusicBundle.PlayList;

import com.Dongo.GodLife.MusicBundle.PlayList.Exception.NotYourPlaylistException;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistPersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class PlaylistPersistenceAdapterStub implements PlaylistPersistenceAdapter {

    private final List<Playlist> playlistList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Playlist save(Playlist playlist) {
        if (playlist.getPlaylistId() == null) {
            playlist.setPlaylistId(idGenerator.getAndIncrement());
            playlistList.add(playlist);
        } else {
            playlistList.removeIf(existingPlaylist -> 
                existingPlaylist.getPlaylistId().equals(playlist.getPlaylistId()));
            playlistList.add(playlist);
        }
        return playlist;
    }

    @Override
    public Page<Playlist> findByUser(User user, Pageable pageable) {
        List<Playlist> userPlaylists = playlistList.stream()
                .filter(playlist -> playlist.getUser().equals(user))
                .collect(Collectors.toList());
        return new PageImpl<>(userPlaylists, pageable, userPlaylists.size());
    }
    
    @Override
    public Page<Playlist> findByUserWithSearch(User user, String search, Pageable pageable) {
        List<Playlist> userPlaylists = playlistList.stream()
                .filter(playlist -> playlist.getUser().equals(user) &&
                        playlist.getPlaylistTitle().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
        return new PageImpl<>(userPlaylists, pageable, userPlaylists.size());
    }
    
    @Override
    public Page<Playlist> findByUserAndIsShared(User user, Boolean isShared, Pageable pageable) {
        List<Playlist> userPlaylists = playlistList.stream()
                .filter(playlist -> playlist.getUser().equals(user) &&
                        playlist.isShared() == isShared)
                .collect(Collectors.toList());
        return new PageImpl<>(userPlaylists, pageable, userPlaylists.size());
    }

    @Override
    public Optional<Playlist> findById(Long playlistId) {
        return playlistList.stream()
                .filter(playlist -> playlist.getPlaylistId().equals(playlistId))
                .findFirst();
    }

    @Override
    public Playlist delete(Playlist playlist) throws NotYourPlaylistException {
        playlistList.removeIf(existingPlaylist -> 
            existingPlaylist.getPlaylistId().equals(playlist.getPlaylistId()));
        return playlist;
    }
    
    @Override
    public Page<Playlist> findByIsShared(Boolean isShared, Pageable pageable) {
        List<Playlist> sharedPlaylists = playlistList.stream()
                .filter(playlist -> playlist.isShared() == isShared)
                .collect(Collectors.toList());
        return new PageImpl<>(sharedPlaylists, pageable, sharedPlaylists.size());
    }
} 