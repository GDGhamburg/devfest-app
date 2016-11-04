package de.devfest.screens.sessiondetails;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface SessionDetailsView extends MvpBase.AuthView {

    String getSessionId();

    void onSessionReceived(Session session, boolean isScheduled);

    void onError(Throwable error);
}
