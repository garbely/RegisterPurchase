package com.example.tv_shows.db.firebase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.example.tv_shows.db.entity.Episode;

public class EpisodeLiveData extends LiveData<Episode> {

    private static final String TAG = "EpisodeLiveData";

    private final DatabaseReference reference;
    private final String showName;
    private final EpisodeLiveData.MyValueEventListener listener = new EpisodeLiveData.MyValueEventListener();

    public EpisodeLiveData(DatabaseReference reference) {
        this.reference = reference;
        showName = reference.getParent().getParent().getKey();
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
            Episode entity = dataSnapshot.getValue(Episode.class);
            try {
                entity.setId(dataSnapshot.getKey());
                entity.setShowName(showName);
                setValue(entity);
            }
            catch (NullPointerException e){}
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}