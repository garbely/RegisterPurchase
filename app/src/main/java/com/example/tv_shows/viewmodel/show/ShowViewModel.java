package com.example.tv_shows.viewmodel.show;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.tv_shows.BaseApp;
import com.example.tv_shows.db.entity.Show;
import com.example.tv_shows.db.repository.ShowRepository;
import com.example.tv_shows.util.OnAsyncEventListener;

public class ShowViewModel extends AndroidViewModel {

    private ShowRepository repository;
    private final MediatorLiveData<Show> observableShow;

    public ShowViewModel(@NonNull Application application,
                         final String showName, ShowRepository repository) {
        super(application);

        this.repository = repository;

        observableShow = new MediatorLiveData<>();
        observableShow.setValue(null);

        if (showName != null) {
            LiveData<Show> show = repository.getShow(showName);
            observableShow.addSource(show, observableShow::setValue);
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String showName;
        private final ShowRepository repository;

        public Factory(@NonNull Application application, String showName) {
            this.application = application;
            this.showName = showName;
            repository = ((BaseApp) application).getShowRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ShowViewModel(application, showName, repository);
        }
    }

    public LiveData<Show> getShow() {
        return observableShow;
    }

    public void createShow(Show show, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getShowRepository()
                .insert(show, callback);
    }

    public void updateShow(Show show, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getShowRepository()
                .update(show, callback);    }

    public void deleteShow(Show show, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getShowRepository()
                .delete(show, callback);    }
}
