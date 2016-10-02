package de.devfest.screens.main;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.EventManager;
import de.devfest.data.UserManager;
import de.devfest.mvpbase.BasePresenter;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {

    private final Lazy<UserManager> userManager;
    private final Lazy<EventManager> eventManager;

    @Inject
    MainActivityPresenter(Lazy<UserManager> userManager, Lazy<EventManager> eventManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
    }

    @Override
    public void attachView(MainActivityView mvpView) {
        super.attachView(mvpView);
        userManager.get()
                .getCurrentUser()
                .onErrorReturn(throwable -> null)
                .flatMap(user -> {
                    if (user == null || !user.admin) {
                        return eventManager.get().isActive();
                    }
                    return Single.just(true);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        success -> getView().onEventStarted(success),
                        error -> getView().onError(error)
                );
    }
}
