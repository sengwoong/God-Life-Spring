package com.Dongo.GodLife.MusicBundle.Music.Service;


import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicRequest;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import lombok.RequiredArgsConstructor;
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


}
