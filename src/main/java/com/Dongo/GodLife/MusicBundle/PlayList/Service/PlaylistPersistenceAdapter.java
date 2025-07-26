package com.Dongo.GodLife.MusicBundle.PlayList.Service;


import com.Dongo.GodLife.MusicBundle.PlayList.Exception.NotYourPlaylistException;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.Dongo.GodLife.User.Model.User;

import java.util.Optional;

public interface PlaylistPersistenceAdapter {

    Playlist save(Playlist playList);

    Page<Playlist> findByUser(User user, Pageable pageable);

    Page<Playlist> findByUserWithSearch(User user, String search, Pageable pageable);
    
    Page<Playlist> findByUserAndIsShared(User user, Boolean isShared, Pageable pageable);

    Optional<Playlist> findById(Long playListId);

    Playlist delete(Playlist playList) throws NotYourPlaylistException;

    Page<Playlist> findByIsShared(Boolean isShared, Pageable pageable);
}   

