package de.devfest.screens.main;

import android.support.annotation.NonNull;

import de.devfest.model.User;
import de.devfest.mvpbase.MvpBase;

public interface MainView extends MvpBase.View {
    void showConnectivity(boolean connected);
    void showUser(@NonNull User user);
    void offerLogin();
    void startGoogleLogin();
    void showAuthenticationFailedError(Throwable error);
}
