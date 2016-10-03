package de.devfest.screens.schedule;

import de.devfest.model.EventPart;
import de.devfest.mvpbase.MvpBase;

public interface ScheduleView extends MvpBase.View {

    void onError(Throwable error);

    void onEventPartReceived(EventPart eventPart);
}
