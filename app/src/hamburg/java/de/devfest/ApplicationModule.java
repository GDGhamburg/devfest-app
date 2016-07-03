package de.devfest;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.devfest.data.DevFestManager;
import de.devfest.data.firebase.FirebaseDevFestManager;
import de.devfest.user.FirebaseUserManager;
import de.devfest.user.UserManager;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    UserManager provideUserManager() {
        return new FirebaseUserManager();
    }

    @Provides
    @Singleton
    DevFestManager provideDevFestManager() {
        return new FirebaseDevFestManager();
    }
}
