package de.devfest.screens.sessions;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SessionsPresenter extends BasePresenter<SessionsView> {

    private final Lazy<SessionManager> sessionManager;

    @Inject
    public SessionsPresenter(Lazy<SessionManager> sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void attachView(SessionsView mvpView) {
        super.attachView(mvpView);
        untilDetach(sessionManager.get().getSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(session -> {
                    getView().addSession(session);
                }, error -> {
                    getView().onError(error);
                })
        );
    }
}
