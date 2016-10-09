package de.devfest.screens.dashboard;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface DashboardView extends MvpBase.AuthView {
    void onScheduledSessionReceived(Session session);
    void onRunningSessionReceived(Session session, boolean scheduled);
    void onError(Throwable error);
    void showLogin();
}
