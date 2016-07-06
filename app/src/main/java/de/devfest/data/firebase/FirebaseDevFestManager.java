package de.devfest.data.firebase;

import com.google.firebase.database.FirebaseDatabase;

import de.devfest.data.DevFestManager;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.StageManager;
import de.devfest.data.TrackManager;

public final class FirebaseDevFestManager implements DevFestManager {

    private final FirebaseSpeakerManager speakerManager;
    private final FirebaseSessionManager sessionManager;
    private final FirebaseStageManager stageManager;
    private final FirebaseTrackManager trackManager;

    public FirebaseDevFestManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        speakerManager = new FirebaseSpeakerManager(database);
        stageManager = new FirebaseStageManager(database);
        trackManager = new FirebaseTrackManager(database);
        sessionManager = new FirebaseSessionManager(database, speakerManager, stageManager, trackManager);
    }


    @Override
    public SpeakerManager speaker() {
        return speakerManager;
    }

    @Override
    public SessionManager sessions() {
        return sessionManager;
    }

    @Override
    public TrackManager tracks() {
        return trackManager;
    }

    @Override
    public StageManager stages() {
        return stageManager;
    }

}
