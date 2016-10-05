package de.devfest.screens.dashboard;

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

public class DashboardPresenter extends BasePresenter<DashboardView> {

    private final Lazy<SessionManager> sessionManager;
    private final Lazy<UserManager> userManager;

    @Inject
    public DashboardPresenter(Lazy<SessionManager> sessionManager, Lazy<UserManager> userManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;
    }

    @Override
    public void attachView(DashboardView mvpView) {
        super.attachView(mvpView);
        untilDetach(
                getScheduledSessions()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                pair -> {
                                    getView().onSessionReceived(pair.first, pair.second);
                                }, error -> {
                                    getView().onError(error);
                                }));
    }

    private Observable<Session> getSessions() {
        return userManager.get().getCurrentUser().toObservable()
                .map(user -> user.schedule)
                .flatMap(ids -> sessionManager.get().getSessions(ids));
    }

    private Observable<Pair<Session, Boolean>> getScheduledSessions() {
        return userManager.get().loggedInState()
                .flatMap(loggedIn -> {
                    if (loggedIn) {
                        return Observable.zip(
                                getSessions(),
                                userManager.get().getCurrentUser().toObservable(),
                                (session, user) -> Pair.create(session, user.schedule.contains(session.id)));
                    } else {
                        return getSessions()
                                .map(session -> Pair.create(session, Boolean.FALSE));
                    }
                });
    }

    public void addToSchedule(Session session) {
        userManager.get().getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> getView().onLoginRequired())
                .flatMap(user -> userManager.get().addToSchedule(session))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        success -> Timber.d("Session added to schedule: %s", success),
                        error -> getView().onError(error)
                );
    }

    public void removeFromSchedule(Session session) {
        userManager.get().getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> getView().onLoginRequired())
                .flatMap(user -> userManager.get().removeFromSchedule(session))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        success -> Timber.d("Session removed from schedule: %s", success),
                        error -> getView().onError(error)
                );
    }

}
