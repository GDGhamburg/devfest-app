package de.devfest.screens.eventpart;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.UserManager;
import de.devfest.model.Session;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventPartPresenter extends BasePresenter<EventPartView> {

    private final Lazy<SessionManager> sessionManager;
    private final Lazy<UserManager> userManager;

    @Inject
    public EventPartPresenter(Lazy<SessionManager> sessionManager, Lazy<UserManager> userManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;
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

    public void addToSchedule(Session session) {
        userManager.get().addToSchedule(session)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                        },
                        error -> getView().onError(error));
    }

    public void removeFromSchedule(Session session) {
        userManager.get().removeFromSchedule(session)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    // don't do anything for now
                }, error -> getView().onError(error));
    }
}
