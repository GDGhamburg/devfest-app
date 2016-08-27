package de.devfest.screens.sessions;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface SessionsView extends MvpBase.View {
    void addSession(Session session);
    void onError(Throwable error);
}
