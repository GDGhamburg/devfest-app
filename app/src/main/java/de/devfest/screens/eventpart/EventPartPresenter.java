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
import timber.log.Timber;

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
                userManager.get().loggedInState()
                        .flatMap(loggedIn -> {
                            Observable<Pair<Session, Boolean>> o;
                            if (loggedIn) {
                                return userManager.get().getCurrentUser().toObservable()
                                        .flatMap(user ->
                                                sessionManager.get()
                                                        .getEventPartSessions(mvpView.getEventPartId(),
                                                                mvpView.getTrackId())
                                                        .map(session -> Pair.create(session, user.schedule.contains(session.id))));
                            } else {
                                return sessionManager.get()
                                        .getEventPartSessions(mvpView.getEventPartId(), mvpView.getTrackId())
                                        .map(session -> Pair.create(session, Boolean.FALSE));
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                pair -> {
                                    getView().onSessionReceived(pair.first, pair.second);
                                }, error -> {
                                    getView().onError(error);
                                }));
    }

    public void addToSchedule(Session session) {
        userManager.get().getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> getView().onLoginRequired())
                .flatMap(user -> userManager.get().addToSchedule(session))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                            Timber.d("Session added to schedule: %s", success);
                        },
                        error -> getView().onError(error));
    }

    public void removeFromSchedule(Session session) {
        userManager.get().getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> getView().onLoginRequired())
                .flatMap(user -> userManager.get().removeFromSchedule(session))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                            Timber.d("Session removed from schedule: %s", success);
                        },
                        error -> getView().onError(error));
    }
}
