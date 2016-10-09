package de.devfest.screens.user;

import android.support.annotation.NonNull;

import de.devfest.model.User;
import de.devfest.mvpbase.MvpBase;

public interface UserView extends MvpBase.AuthView {
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


}

