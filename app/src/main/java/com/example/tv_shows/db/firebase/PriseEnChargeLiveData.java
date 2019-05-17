package com.example.tv_shows.db.firebase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tv_shows.db.entity.PriseEnCharge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PriseEnChargeLiveData extends LiveData<PriseEnCharge> {

    private static final String TAG = "PriseEnChargeLiveData";

    private final DatabaseReference reference;
    private final String fromagerieName;
    private final PriseEnChargeLiveData.MyValueEventListener listener = new PriseEnChargeLiveData.MyValueEventListener();

    public PriseEnChargeLiveData(DatabaseReference reference) {
        this.reference = reference;
        fromagerieName = reference.getParent().getParent().getKey();
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
            PriseEnCharge entity = dataSnapshot.getValue(PriseEnCharge.class);
            try {
                entity.setId(dataSnapshot.getKey());
                entity.setFromagerieName(fromagerieName);
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