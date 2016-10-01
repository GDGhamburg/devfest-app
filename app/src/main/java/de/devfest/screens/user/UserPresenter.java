package de.devfest.screens.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.mvpbase.BasePresenter;
import de.devfest.data.UserManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserPresenter extends BasePresenter<UserView> {

    private final Lazy<UserManager> userManager;

    @Inject
    public UserPresenter(Lazy<UserManager> userManager) {
        this.userManager = userManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        userManager.get().getCurrentUser()
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
        userManager.get().authenticateWithGoogle(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> getView().showUser(user),
                        error -> {
                            getView().showAuthenticationFailedError(error);
                        }
                );
    }
}
