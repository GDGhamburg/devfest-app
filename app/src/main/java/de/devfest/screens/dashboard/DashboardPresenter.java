package de.devfest.screens.dashboard;

import android.support.v4.util.Pair;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.model.Session;
import de.devfest.mvpbase.AuthPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DashboardPresenter extends AuthPresenter<DashboardView> {

    private final Lazy<SessionManager> sessionManager;

    @Inject
    public DashboardPresenter(Lazy<SessionManager> sessionManager, Lazy<UserManager> userManager) {
        super(userManager);
        this.sessionManager = sessionManager;
    }

    @Override
    public void attachView(DashboardView mvpView) {
        super.attachView(mvpView);
        untilDetach(getScheduledSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        session -> getView().onScheduledSessionReceived(session),
                        error -> getView().onError(error)
                )
        );

        untilDetach(Observable.interval(0, 1, TimeUnit.MINUTES) // update every minute
                .switchMap(minute -> userManager.get().loggedInState())
                .flatMap(loggedIn -> {
                    if (loggedIn) {
                        return userManager.get().getCurrentUser().toObservable()
                                .flatMap(user ->
                                        sessionManager.get()
                                                .getCurrentlyRunningSessions()
                                                .map(session -> Pair.create(session, user.schedule.contains(session.id))));

                    } else {
                        return sessionManager.get()
                                .getCurrentlyRunningSessions()
                                .map(session -> Pair.create(session, Boolean.FALSE));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pair -> getView().onRunningSessionReceived(pair.first, pair.second),
                        error -> getView().onError(error)
                )
        );

    }

    private Observable<Session> getScheduledSessions() {
        return userManager.get().loggedInState()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(loggedIn -> getView().showLogin())
                .observeOn(Schedulers.io())
                .filter(loggedIn -> loggedIn)
                .flatMap(state -> userManager.get().getCurrentUser().toObservable())
                .map(user -> user.schedule)
                .flatMap(ids -> sessionManager.get().getSessions(ids));

    }


    public void onAddSessionClick(Session session) {
        userManager.get().getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(user -> userManager.get().addToSchedule(session))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                        },
                        error -> {
                            if (error instanceof NoSuchElementException) {
                                getView().startLogin();
                            } else {
                                getView().onError(error);
                            }
                        });
    }

    public void onRemoveSessionClick(Session session) {
        userManager.get().getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(user -> userManager.get().removeFromSchedule(session))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                        },
                        error -> {
                            if (error instanceof NoSuchElementException) {
                                getView().startLogin();
                            } else {
                                getView().onError(error);
                            }
                        });
    }

    public void onLoginRequested() {
        if (isViewAttached()) {
            getView().startLogin();
        }
    }
}
