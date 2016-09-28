package de.devfest.screens.sessions;

import java.util.List;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface SessionsView extends MvpBase.View {

    /**
     * TODO ugly!
     *
     * @param size tells the view how many pages are available
     */
    void finishedInitializaiton(int size);

    void onSessionsReceived(int requestedPage, List<Session> sessions);

    void onError(Throwable error);
}
