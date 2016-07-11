package de.devfest.screens.schedule;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface ScheduleView extends MvpBase.View {
    void onSessionRetreived(Session session);

    void onError(Throwable error);
}
