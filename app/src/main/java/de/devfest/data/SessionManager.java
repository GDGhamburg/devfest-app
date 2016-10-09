package de.devfest.data;

import java.util.Collection;
import java.util.List;

import de.devfest.model.Session;
import rx.Observable;

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

    /**
     * @param eventPart id of the eventpart in which the emitted items should occur
     * @param trackId   of the track in which the sessions are
     * @return a hot observable emitting sessions which are on the given track and in a specific
     * event part
     */
    Observable<Session> getEventPartSessions(String eventPart, String trackId);

    /**
     * @return a hot observable which contains all currently running sessions
     */
    Observable<Session> getCurrentlyRunningSessions();
}
