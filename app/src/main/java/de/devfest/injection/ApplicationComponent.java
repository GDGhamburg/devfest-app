package de.devfest.injection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import de.devfest.ApplicationModule;
import de.devfest.connectivity.ConnectivityManager;
import de.devfest.data.DevFestManager;
import de.devfest.screens.main.MainActivity;
import de.devfest.screens.schedule.ScheduleFragment;
import de.devfest.screens.speakers.SpeakersFragment;
import de.devfest.screens.user.UserFragment;

@Singleton
@Component(modules = ApplicationModule.class)
public abstract class ApplicationComponent {
    public static ApplicationComponent init(Application application) {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(application)).build();
    }

    public abstract void inject(MainActivity activity);
    public abstract void inject(UserFragment fragment);
    public abstract void inject(ScheduleFragment fragment);
    public abstract void inject(SpeakersFragment fragment);

    abstract ConnectivityManager connectivityManager();
    abstract DevFestManager devFestManager();
}
