package com.example.tv_shows;

import android.app.Application;

import com.example.tv_shows.db.repository.PriseEnChargeRepository;
import com.example.tv_shows.db.repository.FromagerieRepository;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {

    public PriseEnChargeRepository getPriseEnChargeRepository() {
        return PriseEnChargeRepository.getInstance();
    }

    public FromagerieRepository getFromagerieRepository() {
        return FromagerieRepository.getInstance();
    }
}