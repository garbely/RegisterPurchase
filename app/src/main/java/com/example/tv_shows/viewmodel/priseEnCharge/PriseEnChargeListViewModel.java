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

import java.util.List;

public class PriseEnChargeListViewModel extends AndroidViewModel {

    private PriseEnChargeRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<PriseEnCharge>> observablePriseEnCharges;

    public PriseEnChargeListViewModel(@NonNull Application application,
                                      final String fromagerieName, PriseEnChargeRepository repository) {
        super(application);

        this.repository = repository;

        observablePriseEnCharges = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observablePriseEnCharges.setValue(null);

        LiveData<List<PriseEnCharge>> priseEnCharges = repository.getAllEpisodes(fromagerieName);

        // observe the changes of the entities from the database and forward them
        observablePriseEnCharges.addSource(priseEnCharges, observablePriseEnCharges::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final PriseEnChargeRepository repository;
        private final String fromagerieName;

        public Factory(@NonNull Application application, String fromagerieName) {
            this.application = application;
            this.fromagerieName = fromagerieName;
            repository = ((BaseApp) application).getPriseEnChargeRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new PriseEnChargeListViewModel(application, fromagerieName, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<PriseEnCharge>> getPriseEnCharges() {
        return observablePriseEnCharges;
    }

    public void deletePriseEnCharge(PriseEnCharge priseEnCharge, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getPriseEnChargeRepository()
                .delete(priseEnCharge, callback);
    }
}
