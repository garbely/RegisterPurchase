package com.example.tv_shows.db.repository;

import android.arch.lifecycle.LiveData;

import com.example.tv_shows.db.entity.Show;
import com.example.tv_shows.db.firebase.ShowLiveData;
import com.example.tv_shows.db.firebase.ShowsListLiveData;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ShowRepository {

    private static final String TAG = "ShowRepository";
    private static ShowRepository instance;

    private ShowRepository() {}

    public static ShowRepository getInstance() {
        if (instance == null) {
            synchronized (ShowRepository.class) {
                if (instance == null) {
                    instance = new ShowRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Show> getShow(final String name) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(name);
        return new ShowLiveData(reference);
    }

    public LiveData<List<Show>> getAllShows() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("shows");
        return new ShowsListLiveData(reference);
    }

    public void insert(final Show show, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(show.getName())
                .setValue(show, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final Show show, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(show.getName())
                .updateChildren(show.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final Show show, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("shows")
                .child(show.getName())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
