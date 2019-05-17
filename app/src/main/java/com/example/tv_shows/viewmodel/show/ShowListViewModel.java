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

import java.util.List;

public class ShowListViewModel extends AndroidViewModel {

    private ShowRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Show>> observableShows;

    public ShowListViewModel(@NonNull Application application,
                             ShowRepository repository) {
        super(application);

        this.repository = repository;

        observableShows = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableShows.setValue(null);

        LiveData<List<Show>> shows = repository.getAllShows();

        // observe the changes of the entities from the database and forward them
        observableShows.addSource(shows, observableShows::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final ShowRepository repository;

        public Factory(@NonNull Application application) {
            this.application = application;
            repository = ShowRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ShowListViewModel(application, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<Show>> getShows() {
        return observableShows;
    }

    public void deleteShow(Show show, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getShowRepository()
                .delete(show, callback);
    }
}
