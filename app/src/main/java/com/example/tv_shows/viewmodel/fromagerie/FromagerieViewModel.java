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

public class FromagerieViewModel extends AndroidViewModel {

    private FromagerieRepository repository;
    private final MediatorLiveData<Fromagerie> observableFromagerie;

    public FromagerieViewModel(@NonNull Application application,
                               final String fromagerieName, FromagerieRepository repository) {
        super(application);

        this.repository = repository;

        observableFromagerie = new MediatorLiveData<>();
        observableFromagerie.setValue(null);

        if (fromagerieName != null) {
            LiveData<Fromagerie> show = repository.getFromagerie(fromagerieName);
            observableFromagerie.addSource(show, observableFromagerie::setValue);
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String fromagerieName;
        private final FromagerieRepository repository;

        public Factory(@NonNull Application application, String fromagerieName) {
            this.application = application;
            this.fromagerieName = fromagerieName;
            repository = ((BaseApp) application).getFromagerieRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new FromagerieViewModel(application, fromagerieName, repository);
        }
    }

    public LiveData<Fromagerie> getFromagerie() {
        return observableFromagerie;
    }

    public void createFromagerie(Fromagerie fromagerie, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getFromagerieRepository()
                .insert(fromagerie, callback);
    }

    public void updateFromagerie(Fromagerie fromagerie, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getFromagerieRepository()
                .update(fromagerie, callback);    }

    public void deleteFromagerie(Fromagerie fromagerie, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getFromagerieRepository()
                .delete(fromagerie, callback);    }
}
