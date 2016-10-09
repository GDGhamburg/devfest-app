package de.devfest.screens.eventpart;

import android.support.v4.util.Pair;

import java.util.NoSuchElementException;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.UserManager;
import de.devfest.model.Session;
import de.devfest.mvpbase.AuthPresenter;
import de.devfest.ui.SessionAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventPartPresenter extends AuthPresenter<EventPartView>
        implements SessionAdapter.SessionInteractionListener {

    private final Lazy<SessionManager> sessionManager;

    @Inject
    public EventPartPresenter(Lazy<SessionManager> sessionManager, Lazy<UserManager> userManager) {
        super(userManager);
        this.sessionManager = sessionManager;
    }

    @Override
    public void attachView(EventPartView mvpView) {
        super.attachView(mvpView);
        untilDetach(
                userManager.get().loggedInState()
                        .switchMap(loggedIn -> {
                            Observable<Pair<Session, Boolean>> o;
                            if (loggedIn) {
                                return userManager.get().getCurrentUser().toObservable()
                                        .flatMap(user ->
                                                sessionManager.get()
                                                        .getEventPartSessions(mvpView.getEventPartId(),
                                                                mvpView.getTrackId())
                                                        .map(session -> Pair.create(session,
                                                                user.schedule.contains(session.id))));
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

    @Override
    public void onSessionClick(Session session) {

    }

    @Override
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

    @Override
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
}
