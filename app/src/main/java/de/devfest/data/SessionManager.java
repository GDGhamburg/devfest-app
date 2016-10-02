package de.devfest.data;

import java.util.Collection;
import java.util.List;

import de.devfest.model.EventPart;
import de.devfest.model.Session;
import de.devfest.model.Track;
import rx.Observable;
import rx.Single;

public interface SessionManager {
    /**
     * @param id of the session you want to observe
     * @return a hot observable emitting a single session by the given id
     */
    Observable<Session> getSession(String id);

    /**
     * @param ids of the session you want to observe
     * @return a hot observable emitting all sessions by the given ids
     */
    Observable<Session> getSessions(Collection<String> ids);

    /**
     * @return a hot observable emitting all available sessions
     */
    Observable<Session> getSessions();


    Observable<Session> getEventPartSessions(String eventPart, String trackId);

    /*
    Single<List<Session>> getSessionsById(Collection<String> ids);
    Single<List<Session>> getSessions();
    Single<List<Session>> getSessions(Track track, EventPart eventPart);
    */
}
