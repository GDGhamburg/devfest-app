package de.devfest.data;

import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.model.Track;
import rx.Observable;

/**
 * Created by andre on 24/06/16.
 */

public interface DevFestManager {
    Observable<Speaker> getSpeakers();

    Observable<Speaker> getSpeaker(String uid);

    Observable<Session> getSessions();

    Observable<Session> getSession(String id);

    Observable<Track> getTracks();

    Observable<Track> getTrack(String id);
}
