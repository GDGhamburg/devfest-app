package de.devfest.screens.eventpart;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

/**
 * Created by andre on 02.10.2016.
 */

public interface EventPartView extends MvpBase.View {
    String getTrackId();
    String getEventPartId();
    void onSessionReceived(Session session);
    void onError(Throwable error);
}
