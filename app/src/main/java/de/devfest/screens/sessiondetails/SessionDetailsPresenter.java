package de.devfest.screens.sessiondetails;

import android.support.v4.util.Pair;

import java.util.NoSuchElementException;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.UserManager;
import de.devfest.model.Session;
import de.devfest.mvpbase.AuthPresenter;
import de.devfest.ui.SessionAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SessionDetailsPresenter extends AuthPresenter<SessionDetailsView>
        implements SessionAdapter.SessionInteractionListener{

    private final Lazy<SpeakerManager> speakerManager;
    private final Lazy<SessionManager> sessionManager;

    @Inject
    public SessionDetailsPresenter(Lazy<SpeakerManager> speakerManager, Lazy<SessionManager> sessionManager,
                                   Lazy<UserManager> userManager) {
        super(userManager);
        this.speakerManager = speakerManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public void attachView(SessionDetailsView mvpView) {
        super.attachView(mvpView);
        String sessionId = getView().getSessionId();
        untilDetach(
                userManager.get().loggedInState()
                        .switchMap(loggedIn -> {
                            Observable<Pair<Session, Boolean>> o;
                            if (loggedIn) {
                                o = Observable.zip(
                                        sessionManager.get().getSession(sessionId).observeOn(Schedulers.io()),
                                        userManager.get().getCurrentUser().toObservable(),
                                        (session, user) -> Pair.create(session, user.schedule.contains(session.id)));
                            } else {
                                o = sessionManager.get().getSession(sessionId)
                                        .observeOn(Schedulers.io())
                                        .map(session -> Pair.create(session, Boolean.FALSE));
                            }
                            return o;
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
