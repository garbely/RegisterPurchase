package com.example.tv_shows.db.repository;

import android.arch.lifecycle.LiveData;

import com.example.tv_shows.db.entity.Episode;
import com.example.tv_shows.db.firebase.EpisodeListLiveData;
import com.example.tv_shows.db.firebase.EpisodeLiveData;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EpisodeRepository {

    private static EpisodeRepository instance;

    private EpisodeRepository() {}

    public static EpisodeRepository getInstance() {
        if (instance == null) {
            synchronized (EpisodeRepository.class) {
                if (instance == null) {
                    instance = new EpisodeRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Episode> getEpisode(final String episodeId, final String showName) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(showName)
                .child("episodes")
                .child(episodeId);
        return new EpisodeLiveData(reference);
    }

    public LiveData<List<Episode>> getAllEpisodes(final String showName) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(showName)
                .child("episodes");
        return new EpisodeListLiveData(reference, showName);
    }

    public void insert(final Episode episode, OnAsyncEventListener callback) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(episode.getShowName())
                .child("episodes");

        FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(episode.getShowName())
                .child("episodes")
                .child(episode.getId())
                .setValue(episode, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final Episode episode, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(episode.getShowName())
                .child("episodes")
                .child(episode.getId())
                .updateChildren(episode.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final Episode episode, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(episode.getShowName())
                .child("episodes")
                .child(episode.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
