package de.devfest.data;

import java.util.Collection;
import java.util.List;

import de.devfest.model.EventPart;
import de.devfest.model.Session;
import de.devfest.model.Track;
import rx.Single;

public interface SessionManager {
    Single<Session> getSessionById(String id);
    Single<List<Session>> getSessionsById(Collection<String> ids);
    Single<List<Session>> getSessions();
    Single<List<Session>> getSessions(Track track, EventPart eventPart);
}
