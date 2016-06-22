package de.devfest.screens.main;

import de.devfest.mvpbase.MvpBase;

public interface MainView extends MvpBase.View {
    void showConnectivity(boolean connected);
}
