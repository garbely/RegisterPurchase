package com.example.tv_shows.viewmodel.episode;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.tv_shows.BaseApp;
import com.example.tv_shows.db.entity.Episode;
import com.example.tv_shows.db.repository.EpisodeRepository;
import com.example.tv_shows.util.OnAsyncEventListener;

public class EpisodeViewModel extends AndroidViewModel {

    private EpisodeRepository repository;
    private final MediatorLiveData<Episode> observableEpisode;

    public EpisodeViewModel(@NonNull Application application,
                            final String idEpisode, final String showName, EpisodeRepository repository) {
        super(application);

        this.repository = repository;

        observableEpisode = new MediatorLiveData<>();
        observableEpisode.setValue(null);

        if (idEpisode != null){
            LiveData<Episode> account = repository.getEpisode(idEpisode, showName);
            observableEpisode.addSource(account, observableEpisode::setValue);
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String idEpisode;
        private final String showName;
        private final EpisodeRepository repository;

        public Factory(@NonNull Application application, String idEpisode, String showName) {
            this.application = application;
            this.idEpisode = idEpisode;
            this.showName = showName;
            repository = ((BaseApp) application).getEpisodeRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new EpisodeViewModel(application, idEpisode, showName, repository);
        }
    }

    public LiveData<Episode> getEpisode() {
        return observableEpisode;
    }

    public void createEpisode(Episode episode, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getEpisodeRepository()
                .insert(episode, callback);
    }

    public void updateEpisode(Episode episode, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getEpisodeRepository()
                .update(episode, callback);
    }

    public void deleteEpisode(Episode episode, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getEpisodeRepository()
                .delete(episode, callback);
    }
}
