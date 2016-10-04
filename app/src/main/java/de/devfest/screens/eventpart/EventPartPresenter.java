package de.devfest.screens.eventpart;

import android.support.v4.util.Pair;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.UserManager;
import de.devfest.model.Session;
import de.devfest.mvpbase.BasePresenter;
import rx.Observable;
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
                Observable.zip(
                        sessionManager.get()
                                .getEventPartSessions(mvpView.getEventPartId(), mvpView.getTrackId()),
                        userManager.get().getCurrentUser().toObservable()
                        .onErrorResumeNext(error -> null),
                        Pair::create
                )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(pair -> {
                            getView().onSessionReceived(pair.first, pair.second.schedule.contains(pair.first.id));
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
