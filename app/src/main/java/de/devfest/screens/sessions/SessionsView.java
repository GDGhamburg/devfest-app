package de.devfest.screens.sessions;

import de.devfest.model.EventPart;
import de.devfest.mvpbase.MvpBase;

public interface SessionsView extends MvpBase.View {

    void onError(Throwable error);

    void onEventPartReceived(EventPart eventPart);
}
