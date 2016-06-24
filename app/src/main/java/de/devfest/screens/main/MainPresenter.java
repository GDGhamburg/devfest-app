package de.devfest.screens.main;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.inject.Inject;

import de.devfest.connectivity.ConnectivityManager;
import de.devfest.data.DevFestManager;
import de.devfest.model.Speaker;
import de.devfest.model.User;
import de.devfest.mvpbase.BasePresenter;
import de.devfest.user.UserManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainView> {

    private final ConnectivityManager connectivityManager;
    private final UserManager userManager;
    private final DevFestManager devfest;

    @Inject
    public MainPresenter(ConnectivityManager connectivityManager, UserManager userManager, DevFestManager devfest) {
        this.connectivityManager = connectivityManager;
        this.userManager = userManager;
        this.devfest = devfest;
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
        userManager.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> getView().showUser(user)
                        , error -> getView().offerLogin());
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

    public void test() {
        devfest.getSpeakers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speaker -> {
                    Timber.w(speaker.name);
                });

    }


}
