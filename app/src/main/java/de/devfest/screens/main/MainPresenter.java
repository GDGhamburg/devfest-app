package de.devfest.screens.main;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.inject.Inject;

import de.devfest.connectivity.ConnectivityManager;
import de.devfest.model.User;
import de.devfest.mvpbase.BasePresenter;
import de.devfest.user.UserManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainView> {

    private final ConnectivityManager connectivityManager;
    private final UserManager userManager;

    @Inject
    public MainPresenter(ConnectivityManager connectivityManager, UserManager userManager) {
        this.connectivityManager = connectivityManager;
        this.userManager = userManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        untilOnPause(connectivityManager
                .observeInternetConnectivity()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connected -> {
                    getView().showConnectivity(connected);
                }));
        User user = userManager.getCurrentUser();
        // if not logged in
        if (user == null) {
            getView().offerLogin();
        } else {
            getView().showUser(user);
        }
    }

    public void loginRequested() {
        getView().startGoogleLogin();
    }

    public void authenticationFailed() {
        getView().showAuthenticationFailedError(new Exception("Login failed!"));
    }

    public void googleSigninSuccessful(GoogleSignInAccount account) {
        userManager.authenticateWithGoogle(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> getView().showUser(user),
                        error -> {
                            getView().showAuthenticationFailedError(error);
                        }
                );
    }


}
