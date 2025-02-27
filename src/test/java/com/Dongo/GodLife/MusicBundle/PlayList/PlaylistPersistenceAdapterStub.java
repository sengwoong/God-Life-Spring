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
    public Optional<Playlist> findById(long playlistId) {
        return playlistList.stream()
                .filter(playlist -> playlist.getPlaylistId() == playlistId)
                .findFirst();
    }

    @Override
    public Playlist delete(Playlist playlist) throws NotYourPlaylistException {
        if (playlist == null) {
            throw new NotYourPlaylistException("Playlist cannot be null");
        }
        playlistList.removeIf(existingPlaylist -> 
            existingPlaylist.getPlaylistId().equals(playlist.getPlaylistId()));
        return playlist;
    }
} 