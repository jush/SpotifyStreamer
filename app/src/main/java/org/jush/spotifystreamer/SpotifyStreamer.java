package org.jush.spotifystreamer;

import android.app.Application;
import android.os.StrictMode;

import timber.log.Timber;

public class SpotifyStreamer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new Timber.DebugTree());
        }
    }
}
