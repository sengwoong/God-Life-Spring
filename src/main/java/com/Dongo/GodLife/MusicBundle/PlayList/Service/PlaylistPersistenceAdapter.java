package com.Dongo.GodLife.MusicBundle.PlayList.Service;


import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.Dongo.GodLife.User.Model.User;

import java.util.Optional;

public interface PlaylistPersistenceAdapter {

    Playlist save(Playlist playList);

    Page<Playlist> findByUser(User user, Pageable pageable);

    Optional<Playlist> findById(long playListId);

}

