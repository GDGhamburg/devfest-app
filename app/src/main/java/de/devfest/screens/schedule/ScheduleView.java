package de.devfest.screens.schedule;

import java.util.List;

import de.devfest.model.Session;
import de.devfest.mvpbase.MvpBase;

public interface ScheduleView extends MvpBase.View {
    void onScheduleSessionReceived(List<Session> session);
    void onError(Throwable error);
}
