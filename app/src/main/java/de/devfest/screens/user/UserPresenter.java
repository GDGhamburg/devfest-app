package de.devfest.screens.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.inject.Inject;

import de.devfest.connectivity.ConnectivityManager;
import de.devfest.mvpbase.BasePresenter;
import de.devfest.user.UserManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserPresenter extends BasePresenter<UserView> {

    private final ConnectivityManager connectivityManager;
    private final UserManager userManager;

    @Inject
    public UserPresenter(ConnectivityManager connectivityManager, UserManager userManager) {
        this.connectivityManager = connectivityManager;
        this.userManager = userManager;
    }

    @Override
    public void onResume() {
        super.onResume();
//        untilOnPause(connectivityManager
//                .observeInternetConnectivity()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(connected -> {
//                    getView().showConnectivity(connected);
//                }));
        userManager.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> getView().showUser(user)
                        , error -> getView().showLogin());
    }

    public void requestLogin() {
        getView().startGoogleLogin();
    }

    public void onAuthenticationFailed() {
        getView().showAuthenticationFailedError(new Exception("Login failed!"));
    }

    public void onGoogleSignInSuccessful(GoogleSignInAccount account) {
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
