package com.example.tv_shows.viewmodel.priseEnCharge;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.tv_shows.BaseApp;
import com.example.tv_shows.db.entity.PriseEnCharge;
import com.example.tv_shows.db.repository.PriseEnChargeRepository;
import com.example.tv_shows.util.OnAsyncEventListener;

public class PriseEnChargeViewModel extends AndroidViewModel {

    private PriseEnChargeRepository repository;
    private final MediatorLiveData<PriseEnCharge> observablePriseEnCharge;

    public PriseEnChargeViewModel(@NonNull Application application,
                                  final String idPriseEnCharge, final String fromagerieName, PriseEnChargeRepository repository) {
        super(application);

        this.repository = repository;

        observablePriseEnCharge = new MediatorLiveData<>();
        observablePriseEnCharge.setValue(null);

        if (idPriseEnCharge != null){
            LiveData<PriseEnCharge> account = repository.getPriseEnCharge(idPriseEnCharge, fromagerieName);
            observablePriseEnCharge.addSource(account, observablePriseEnCharge::setValue);
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String idPriseEnCharge;
        private final String fromagerieName;
        private final PriseEnChargeRepository repository;

        public Factory(@NonNull Application application, String idPriseEnCharge, String fromagerieName) {
            this.application = application;
            this.idPriseEnCharge = idPriseEnCharge;
            this.fromagerieName = fromagerieName;
            repository = ((BaseApp) application).getPriseEnChargeRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new PriseEnChargeViewModel(application, idPriseEnCharge, fromagerieName, repository);
        }
    }

    public LiveData<PriseEnCharge> getPriseEnCharge() {
        return observablePriseEnCharge;
    }

    public void createPriseEnCharge(PriseEnCharge priseEnCharge, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getPriseEnChargeRepository()
                .insert(priseEnCharge, callback);
    }

    public void updatePriseEnCharge(PriseEnCharge priseEnCharge, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getPriseEnChargeRepository()
                .update(priseEnCharge, callback);
    }

    public void deletePriseEnCharge(PriseEnCharge priseEnCharge, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getPriseEnChargeRepository()
                .delete(priseEnCharge, callback);
    }
}
