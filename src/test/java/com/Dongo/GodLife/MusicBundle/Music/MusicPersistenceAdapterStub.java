package com.Dongo.GodLife.MusicBundle.Music;

import com.Dongo.GodLife.MusicBundle.Music.Exception.NotYourMusicException;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicPersistenceAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MusicPersistenceAdapterStub implements MusicPersistenceAdapter {

    private final List<Music> musicList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Music save(Music music) {
        if (music.getMusicId() == null) {
            music.setMusicId(idGenerator.getAndIncrement());
            musicList.add(music);
        } else {
            musicList.removeIf(existingMusic -> existingMusic.getMusicId().equals(music.getMusicId()));
            musicList.add(music);
        }
        return music;
    }

    @Override
    public Page<Music> findPlaylistMusics(long playlistId, Pageable pageable) {
        List<Music> playlistMusics = musicList.stream()
                .filter(music -> music.getPlaylist() != null && 
                        music.getPlaylist().getPlaylistId() == playlistId)
                .collect(Collectors.toList());
        return new PageImpl<>(playlistMusics, pageable, playlistMusics.size());
    }

    @Override
    public Optional<Music> findById(long musicId) {
        return musicList.stream()
                .filter(music -> music.getMusicId() == musicId)
                .findFirst();
    }

    @Override
    public Music delete(Music music) throws NotYourMusicException {
        if (music == null) {
            throw new NotYourMusicException("Music cannot be null");
        }
        musicList.removeIf(existingMusic -> existingMusic.getMusicId().equals(music.getMusicId()));
        return music;
    }
} 