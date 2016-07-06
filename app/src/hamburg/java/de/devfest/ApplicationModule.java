package de.devfest;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

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
    private final FirebaseDatabase database;

    public ApplicationModule(Application application) {
        this.application = application;
        this.database = FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    UserManager provideUserManager() {
        return new FirebaseUserManager();
    }

    @Provides
    @Singleton
    SpeakerManager provideSpeakerManager() {
        return new FirebaseSpeakerManager(database);
    }

    @Provides
    @Singleton
    StageManager provideStageManager() {
        return new FirebaseStageManager(database);
    }

    @Provides
    @Singleton
    TrackManager provideTrackManager() {
        return new FirebaseTrackManager(database);
    }

    @Provides
    @Singleton
    SessionManager provideSessionManager(TrackManager trackManager, StageManager stageManager,
                                         SpeakerManager speakerManager) {
        return new FirebaseSessionManager(database, speakerManager, stageManager, trackManager);
    }
}
