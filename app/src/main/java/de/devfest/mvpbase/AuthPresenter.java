package de.devfest.mvpbase;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import dagger.Lazy;
import de.devfest.data.UserManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class AuthPresenter<V extends MvpBase.AuthView> extends BasePresenter<V> {

    protected final Lazy<UserManager> userManager;

    protected AuthPresenter(Lazy<UserManager> userManager) {
        this.userManager = userManager;
    }

    public void onGoogleSignInSuccessful(GoogleSignInAccount account) {
        userManager.get().authenticateWithGoogle(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> getView().onSuccessfullLogin(user),
                        error -> getView().onError(error)
                );
    }

    public void onAuthenticationFailed(String message) {
        //getView().onError(new GoogleAuthException(message));
    }
}
