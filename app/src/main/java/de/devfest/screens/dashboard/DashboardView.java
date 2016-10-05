package de.devfest.screens.dashboard;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface DashboardView extends MvpBase.View {
    void onSessionReceived(Session session, boolean scheduled);
    void onError(Throwable error);
}
