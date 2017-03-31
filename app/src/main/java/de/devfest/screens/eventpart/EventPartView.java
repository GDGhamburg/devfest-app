package de.devfest.screens.eventpart;

import de.devfest.model.EventPart;
import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface EventPartView extends MvpBase.AuthView {
    String getTrackId();

    String getEventPartId();

    void onSessionReceived(Session session, boolean scheduled);

    void onError(Throwable error);

    void onEventPartReceived(EventPart eventPart);
}
