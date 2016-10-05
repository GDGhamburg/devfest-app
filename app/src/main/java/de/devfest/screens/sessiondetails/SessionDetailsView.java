package de.devfest.screens.sessiondetails;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface SessionDetailsView extends MvpBase.View {

    String getSessionId();

    void onSessionAvailable(Session session);

    void onError(Throwable error);
}
