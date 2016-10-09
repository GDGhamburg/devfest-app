package de.devfest.screens.user;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.UserManager;
import de.devfest.mvpbase.AuthPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserPresenter extends AuthPresenter<UserView> {


    @Inject
    public UserPresenter(Lazy<UserManager> userManager) {
        super(userManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        userManager.get().getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> getView().showUser(user),
                        error -> getView().showLogin()
                );
    }

    public void requestLogin() {
        getView().startLogin();
    }
}
