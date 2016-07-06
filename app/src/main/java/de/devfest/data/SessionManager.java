package de.devfest.data;

import de.devfest.model.Session;
import rx.Observable;

/**
 * Created by andre on 06.07.2016.
 */

public interface SessionManager {
    Observable<Session> getSessions();
}
