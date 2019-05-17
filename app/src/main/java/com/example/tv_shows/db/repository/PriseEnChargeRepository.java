package com.example.tv_shows.db.repository;

import android.arch.lifecycle.LiveData;

import com.example.tv_shows.db.entity.PriseEnCharge;
import com.example.tv_shows.db.firebase.PriseEnChargesListLiveData;
import com.example.tv_shows.db.firebase.PriseEnChargeLiveData;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PriseEnChargeRepository {

    private static PriseEnChargeRepository instance;

    private PriseEnChargeRepository() {}

    public static PriseEnChargeRepository getInstance() {
        if (instance == null) {
            synchronized (PriseEnChargeRepository.class) {
                if (instance == null) {
                    instance = new PriseEnChargeRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<PriseEnCharge> getPriseEnCharge(final String priseEnChargeId, final String fromagerieName) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("fromageries")
                .child(fromagerieName)
                .child("priseEnCharges")
                .child(priseEnChargeId);
        return new PriseEnChargeLiveData(reference);
    }

    public LiveData<List<PriseEnCharge>> getAllEpisodes(final String fromagerieName) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("fromageries")
                .child(fromagerieName)
                .child("priseEnCharges");
        return new PriseEnChargesListLiveData(reference, fromagerieName);
    }

    public void insert(final PriseEnCharge priseEnCharge, OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("fromageries").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("fromageries")
                .child(priseEnCharge.getFromagerieName())
                .child("priseEnCharges")
                .child(id)
                .setValue(priseEnCharge, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final PriseEnCharge priseEnCharge, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("fromageries")
                .child(priseEnCharge.getFromagerieName())
                .child("priseEnCharges")
                .child(priseEnCharge.getId())
                .updateChildren(priseEnCharge.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final PriseEnCharge priseEnCharge, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("fromageries")
                .child(priseEnCharge.getFromagerieName())
                .child("priseEnCharges")
                .child(priseEnCharge.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
