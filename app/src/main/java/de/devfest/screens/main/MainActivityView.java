package de.devfest.screens.main;

import de.devfest.mvpbase.MvpBase;

public interface MainActivityView extends MvpBase.View {
    void onEventStarted(boolean started);
    void onError(Throwable error);
}
