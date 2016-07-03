package de.devfest.screens.user;

import android.support.annotation.NonNull;

import de.devfest.model.User;
import de.devfest.mvpbase.MvpBase;

public interface UserView extends MvpBase.View {
    /**
     * Called when authentication was successful
     *
     * @param user which is logged in
     */
    void showUser(@NonNull User user);

    /**
     * Called when the user is not logged in
     */
    void showLogin();

    /**
     * Called when the user pressed the login button
     */
    void startGoogleLogin();

    /**
     * Called when there was an error while loggin in
     *
     * @param error that happened
     */
    void showAuthenticationFailedError(Throwable error);
}

