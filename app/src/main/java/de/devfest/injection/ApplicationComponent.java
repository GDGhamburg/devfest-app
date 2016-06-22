package de.devfest.injection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import de.devfest.connectivity.ConnectivityManager;
import de.devfest.screens.main.MainActivity;

@Singleton
@Component(modules = ApplicationModule.class)
public abstract class ApplicationComponent {
    public static ApplicationComponent init(Application application) {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(application)).build();
    }

    public abstract void inject(MainActivity activity);

    abstract ConnectivityManager connectivityManager();
}
