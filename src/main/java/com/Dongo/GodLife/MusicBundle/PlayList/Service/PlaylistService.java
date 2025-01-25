package com.Dongo.GodLife.MusicBundle.PlayList.Service;



import com.Dongo.GodLife.MusicBundle.Dto.PlaylistRequest;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistPersistenceAdapter playlistRepository;

    public Playlist createPlaylist(PlaylistRequest playListRequest, User user) {

        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setPlaylistTitle(playListRequest.getPlayListTitle());
        return playlistRepository.save(playlist);
    }

    public Page<Playlist> getAllPlaylistsByUserId( User user,Pageable pageable) {
        return playlistRepository.findByUser(user, pageable);
    }
}
