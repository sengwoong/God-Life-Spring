package com.Dongo.GodLife.MusicBundle.PlayList.Repository;


import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistPersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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
}
