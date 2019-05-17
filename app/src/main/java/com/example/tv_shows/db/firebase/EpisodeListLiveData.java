package com.example.tv_shows.db.firebase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tv_shows.db.entity.Episode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EpisodeListLiveData extends LiveData<List<Episode>> {

    private static final String TAG = "EpisodeListLiveData";

    private final DatabaseReference reference;
    private final String showName;
    private final MyValueEventListener listener = new MyValueEventListener();

    public EpisodeListLiveData(DatabaseReference reference, String showName) {
        this.reference= reference;
        this.showName = showName;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toAccounts(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<Episode> toAccounts(DataSnapshot snapshot) {
        List<Episode> accounts = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Episode entity = childSnapshot.getValue(Episode.class);
            entity.setId(childSnapshot.getKey());
            entity.setShowName(showName);
            accounts.add(entity);
        }
        return accounts;
    }
}
