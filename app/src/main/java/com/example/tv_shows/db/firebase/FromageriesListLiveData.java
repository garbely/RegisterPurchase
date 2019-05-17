package com.example.tv_shows.db.firebase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tv_shows.db.entity.Fromagerie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FromageriesListLiveData extends LiveData<List<Fromagerie>> {

    private static final String TAG = "FromageriesListLiveData";

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    public FromageriesListLiveData(DatabaseReference reference) {
        this.reference=reference;
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
            setValue(toFromageries(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<Fromagerie> toFromageries(DataSnapshot snapshot) {
        List<Fromagerie> fromageries = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Fromagerie entity = childSnapshot.getValue(Fromagerie.class);
            entity.setName(childSnapshot.getKey());
            fromageries.add(entity);
        }
        return fromageries;
    }
}