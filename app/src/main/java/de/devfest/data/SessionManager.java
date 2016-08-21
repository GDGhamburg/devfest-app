package de.devfest.data;

import org.threeten.bp.ZonedDateTime;

import de.devfest.model.Session;
import rx.Observable;

public interface SessionManager {
    Observable<Session> getSessionById(String id);
    Observable<Session> getSessions();
    Observable<Session> getSessions(ZonedDateTime from, ZonedDateTime to);
}
