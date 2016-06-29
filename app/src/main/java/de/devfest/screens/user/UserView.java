package de.devfest.screens.user;

import de.devfest.model.User;
import de.devfest.mvpbase.MvpBase;

public interface UserView extends MvpBase.View {
    void showUser(User user);
    void showLogin();
    void startGoogleLogin();
    void showAuthenticationFailedError(Throwable error);
}

