package org.jush.spotifystreamer;

import android.app.Application;

import timber.log.Timber;

public class SpotifyStreamer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
