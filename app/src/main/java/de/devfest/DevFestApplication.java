package de.devfest;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import de.devfest.injection.ApplicationComponent;
import timber.log.Timber;

public class DevFestApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = ApplicationComponent.init(this);
        AndroidThreeTen.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
