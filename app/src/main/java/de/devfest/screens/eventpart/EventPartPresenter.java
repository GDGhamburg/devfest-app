package de.devfest.screens.eventpart;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventPartPresenter extends BasePresenter<EventPartView> {

    private final Lazy<SessionManager> sessionManager;

    @Inject
    public EventPartPresenter(Lazy<SessionManager> sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void attachView(EventPartView mvpView) {
        super.attachView(mvpView);
        untilDetach(
                sessionManager.get()
                        .getEventPartSessions(mvpView.getEventPartId(), mvpView.getTrackId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(session -> {
                            getView().onSessionReceived(session);
                        }, error -> getView().onError(error))
        );
    }
}
