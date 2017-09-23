package de.devfest;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import de.devfest.data.ContributorManager;
import de.devfest.data.EventManager;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.StageManager;
import de.devfest.data.TrackManager;
import de.devfest.data.UserManager;
import de.devfest.data.firebase.FirebaseEventManager;
import de.devfest.data.firebase.FirebaseSessionManager;
import de.devfest.data.firebase.FirebaseSpeakerManager;
import de.devfest.data.firebase.FirebaseStageManager;
import de.devfest.data.firebase.FirebaseTrackManager;
import de.devfest.data.firebase.FirebaseUserManager;
import de.devfest.data.github.GithubContributorManager;

@Module
public class ApplicationModule {

    private final Application application;
    private final FirebaseDatabase database;

    public ApplicationModule(Application application) {
        this.application = application;
        this.database = FirebaseDatabase.getInstance();
        this.database.setPersistenceEnabled(true);
    }

    @Provides
    @Singleton
    UserManager provideUserManager() {
        return new FirebaseUserManager(database);
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
    SessionManager provideSessionManager(Lazy<SpeakerManager> speakerManager,
                                         Lazy<StageManager> stageManager,
                                         Lazy<TrackManager> trackManager,
                                         Lazy<EventManager> eventManager) {
        return new FirebaseSessionManager(database, speakerManager, stageManager, trackManager,
                eventManager);
    }

    @Provides
    @Singleton
    EventManager provideEventManager(Lazy<TrackManager> trackManager) {
        return new FirebaseEventManager(database, trackManager);
    }

    @Provides
    @Singleton
    ContributorManager provideContributorManager() {
        return new GithubContributorManager();
    }
}
