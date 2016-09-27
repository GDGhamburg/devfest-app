package de.devfest.data;

import org.threeten.bp.ZonedDateTime;

import de.devfest.model.Session;
import de.devfest.model.Track;
import rx.Observable;

public interface SessionManager {
    Observable<Session> getSessionById(String id);
    Observable<Session> getSessions();
    Observable<Session> getSessions(Track track, ZonedDateTime from, ZonedDateTime to);
    Observable<Session> getSessionsByTrack(String trackId);
}
