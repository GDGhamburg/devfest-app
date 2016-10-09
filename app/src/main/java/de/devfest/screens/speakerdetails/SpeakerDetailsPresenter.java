package de.devfest.screens.speakerdetails;

import android.support.v4.util.Pair;

import java.util.NoSuchElementException;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.UserManager;
import de.devfest.model.Session;
import de.devfest.model.SocialLink;
import de.devfest.mvpbase.AuthPresenter;
import de.devfest.ui.SessionAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SpeakerDetailsPresenter extends AuthPresenter<SpeakerDetailsView>
        implements SessionAdapter.SessionInteractionListener {

    private final Lazy<SpeakerManager> speakerManager;
    private final Lazy<SessionManager> sessionManager;

    @Inject
    public SpeakerDetailsPresenter(Lazy<SpeakerManager> speakerManager, Lazy<SessionManager> sessionManager,
                                   Lazy<UserManager> userManager) {
        super(userManager);
        this.speakerManager = speakerManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public void attachView(SpeakerDetailsView mvpView) {
        super.attachView(mvpView);
        String speakerId = getView().getSpeakerId();
        untilDetach(
                userManager.get().loggedInState()
                        .flatMap(loggedIn -> {
                            Observable<Pair<Session, Boolean>> o;
                            if (loggedIn) {
                                o = Observable.zip(
                                        speakerManager.get().getSpeaker(speakerId)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .doOnNext(speaker -> getView().onSpeakerAvailable(speaker))
                                                .observeOn(Schedulers.io())
                                                .flatMap(speaker -> sessionManager.get().getSessions(speaker.sessions)),
                                        userManager.get().getCurrentUser().toObservable(),
                                        (session, user) -> Pair.create(session, user.schedule.contains(session.id)));
                            } else {
                                o = speakerManager.get().getSpeaker(speakerId)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnNext(speaker -> getView().onSpeakerAvailable(speaker))
                                        .observeOn(Schedulers.io())
                                        .flatMap(speaker -> sessionManager.get().getSessions(speaker.sessions))
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

    public void onLinkClick(SocialLink link) {
        getView().openLink(link.link);
    }

    @Override
    public void onSessionClick(Session session) {
        getView().showSessionDetails(session.id);
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
