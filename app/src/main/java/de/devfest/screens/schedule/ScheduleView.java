package de.devfest.screens.schedule;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface ScheduleView extends MvpBase.View {
    void onScheduleSessionReceived(Session session);
    void onError(Throwable error);
}
