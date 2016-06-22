package de.devfest.injection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.devfest.connectivity.ConnectivityManager;
import de.devfest.connectivity.DevFestConnectivityManager;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager() {
        return new DevFestConnectivityManager(application);
    }
}
