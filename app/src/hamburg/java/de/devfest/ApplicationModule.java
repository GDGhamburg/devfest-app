package de.devfest;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.StageManager;
import de.devfest.data.TrackManager;
import de.devfest.data.firebase.FirebaseSessionManager;
import de.devfest.data.firebase.FirebaseSpeakerManager;
import de.devfest.data.firebase.FirebaseStageManager;
import de.devfest.data.firebase.FirebaseTrackManager;
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
    SpeakerManager provideSpeakerManager() {
        return new FirebaseSpeakerManager();
    }

    @Provides
    @Singleton
    StageManager provideStageManager() {
        return new FirebaseStageManager();
    }

    @Provides
    @Singleton
    TrackManager provideTrackManager() {
        return new FirebaseTrackManager();
    }

    @Provides
    @Singleton
    SessionManager provideSessionManager(TrackManager trackManager, StageManager stageManager,
                                         SpeakerManager speakerManager) {
        return new FirebaseSessionManager(speakerManager, stageManager, trackManager);
    }
}
