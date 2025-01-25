package com.Dongo.GodLife.MusicBundle.Music.Service;


import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicRequest;
import com.Dongo.GodLife.MusicBundle.Music.Exception.NotYourMusicException;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicPersistenceAdapter musicRepository;


    public Music createMusic(MusicRequest musicRequest) {

        Music music = new Music();
        music.setMusicTitle(musicRequest.getMusicTitle());
        music.setMusicUrl(musicRequest.getMusicUrl());

        return musicRepository.save(music);
    }

    public Page<Music> getAllMusicByPlaylist(long playlistId, Pageable pageable) {
        return musicRepository.findPlaylistMusics(playlistId,pageable);
    }

    public Music updateMusic(long musicId, User user, MusicRequest musicRequest) {
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new EntityNotFoundException("Music not found with id: " + musicId));

        if (!music.getPlaylist().getUser().equals(user)) {
            throw new NotYourMusicException("Access denied: User does not own the music");
        }

        music.setMusicTitle(musicRequest.getMusicTitle());
        music.setMusicUrl(musicRequest.getMusicUrl());

        return musicRepository.save(music);
    }

    public void deleteMusic(long musicId, User user) {
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new EntityNotFoundException("Music not found with id: " + musicId));

        if (!music.getPlaylist().getPlaylistId().equals(user.getId())) {
            throw new NotYourMusicException("You are not the owner of this music.");
        }

        musicRepository.delete(music);
    }
}
