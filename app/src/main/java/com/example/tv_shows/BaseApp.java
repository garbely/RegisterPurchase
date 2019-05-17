package com.example.tv_shows;

import android.app.Application;

import com.example.tv_shows.db.repository.EpisodeRepository;
import com.example.tv_shows.db.repository.ShowRepository;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {

    public EpisodeRepository getEpisodeRepository() {
        return EpisodeRepository.getInstance();
    }

    public ShowRepository getShowRepository() {
        return ShowRepository.getInstance();
    }
}