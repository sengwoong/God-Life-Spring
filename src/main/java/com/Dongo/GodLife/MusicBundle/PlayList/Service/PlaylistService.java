package com.Dongo.GodLife.MusicBundle.PlayList.Service;



import com.Dongo.GodLife.MusicBundle.PlayList.Dto.PlaylistRequest;
import com.Dongo.GodLife.MusicBundle.PlayList.Exception.NotYourPlaylistException;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.Validator;

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

    public Playlist updatePlayList(long playListId,long user_id, PlaylistRequest playListRequest) throws NotYourPlaylistException {
        Optional<Playlist> optionalPlayList = playlistRepository.findById(playListId);

        if (!optionalPlayList.isPresent()) {
            throw new EntityNotFoundException("PlayList not found with ID: " + playListId);
        }

        Validator.validateNotEmpty(playListRequest.getPlayListTitle(), "PlayList name cannot be empty");

        if(!optionalPlayList.get().getUser().getId().equals(user_id)){
            throw new NotYourPlaylistException("Access denied: User does not own the playlist");
        }
        Playlist playlist = optionalPlayList.get();
        playlist.setPlaylistTitle(playListRequest.getPlayListTitle());

        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(long postId,long userId) throws NotYourPlaylistException {
        Playlist playlist = playlistRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with id: " + postId));
        if (!playlist.getUser().getId().equals(userId)) {
            throw new NotYourPlaylistException("Access denied: User does not own the playlist");
        }
        playlistRepository.delete(playlist);
    }
}
