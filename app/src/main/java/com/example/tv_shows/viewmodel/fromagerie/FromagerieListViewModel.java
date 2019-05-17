package com.example.tv_shows.viewmodel.fromagerie;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.tv_shows.BaseApp;
import com.example.tv_shows.db.entity.Fromagerie;
import com.example.tv_shows.db.repository.FromagerieRepository;
import com.example.tv_shows.util.OnAsyncEventListener;

import java.util.List;

public class FromagerieListViewModel extends AndroidViewModel {

    private FromagerieRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Fromagerie>> observableFromageries;

    public FromagerieListViewModel(@NonNull Application application,
                                   FromagerieRepository repository) {
        super(application);

        this.repository = repository;

        observableFromageries = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableFromageries.setValue(null);

        LiveData<List<Fromagerie>> fromageries = repository.getAllFromageries();

        // observe the changes of the entities from the database and forward them
        observableFromageries.addSource(fromageries, observableFromageries::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final FromagerieRepository repository;

        public Factory(@NonNull Application application) {
            this.application = application;
            repository = FromagerieRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new FromagerieListViewModel(application, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<Fromagerie>> getFromageries() {
        return observableFromageries;
    }

    public void deleteFromagerie(Fromagerie fromagerie, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getFromagerieRepository()
                .delete(fromagerie, callback);
    }
}
