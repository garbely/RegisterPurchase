package com.example.tv_shows.db.repository;

import android.arch.lifecycle.LiveData;

import com.example.tv_shows.db.entity.Fromagerie;
import com.example.tv_shows.db.firebase.FromagerieLiveData;
import com.example.tv_shows.db.firebase.FromageriesListLiveData;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FromagerieRepository {

    private static final String TAG = "FromagerieRepository";
    private static FromagerieRepository instance;

    private FromagerieRepository() {}

    public static FromagerieRepository getInstance() {
        if (instance == null) {
            synchronized (FromagerieRepository.class) {
                if (instance == null) {
                    instance = new FromagerieRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Fromagerie> getFromagerie(final String name) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("fromageries")
                .child(name);
        return new FromagerieLiveData(reference);
    }

    public LiveData<List<Fromagerie>> getAllFromageries() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("fromageries");
        return new FromageriesListLiveData(reference);
    }

    public void insert(final Fromagerie fromagerie, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("fromageries")
                .child(fromagerie.getName())
                .setValue(fromagerie, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final Fromagerie fromagerie, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("fromageries")
                .child(fromagerie.getName())
                .updateChildren(fromagerie.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final Fromagerie fromagerie, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("fromageries")
                .child(fromagerie.getName())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
