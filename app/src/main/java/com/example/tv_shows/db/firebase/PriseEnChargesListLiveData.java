package com.example.tv_shows.db.firebase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tv_shows.db.entity.PriseEnCharge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PriseEnChargesListLiveData extends LiveData<List<PriseEnCharge>> {

    private static final String TAG = "PriseEnChargesListLiveData";

    private final DatabaseReference reference;
    private final String fromagerieName;
    private final MyValueEventListener listener = new MyValueEventListener();

    public PriseEnChargesListLiveData(DatabaseReference reference, String fromagerieName) {
        this.reference= reference;
        this.fromagerieName = fromagerieName;
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
            setValue(toPriseEnCharges(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<PriseEnCharge> toPriseEnCharges(DataSnapshot snapshot) {
        List<PriseEnCharge> priseEnCharges = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            PriseEnCharge entity = childSnapshot.getValue(PriseEnCharge.class);
            entity.setId(childSnapshot.getKey());
            entity.setFromagerieName(fromagerieName);
            priseEnCharges.add(entity);
        }
        return priseEnCharges;
    }
}
