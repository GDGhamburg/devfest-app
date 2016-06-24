package de.devfest.data;

import com.google.firebase.database.FirebaseDatabase;

import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.model.Track;
import rx.Observable;

public class FirebaseDevFestManager implements DevFestManager {


    private final FirebaseDatabase database;

    public FirebaseDevFestManager() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public Observable<Speaker> getSpeakers() {

        return null;
    }

    @Override
    public Observable<Speaker> getSpeaker(String uid) {
        return null;
    }

    @Override
    public Observable<Session> getSessions() {
        return null;
    }

    @Override
    public Observable<Session> getSession(String id) {
        return null;
    }

    @Override
    public Observable<Track> getTracks() {
        return null;
    }

    @Override
    public Observable<Track> getTrack(String id) {
        return null;
    }
}
