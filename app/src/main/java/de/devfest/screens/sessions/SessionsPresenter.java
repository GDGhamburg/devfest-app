package de.devfest.screens.sessions;

import android.support.v4.util.Pair;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.EventManager;
import de.devfest.data.SessionManager;
import de.devfest.data.TrackManager;
import de.devfest.model.EventPart;
import de.devfest.model.Track;
import de.devfest.mvpbase.BasePresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SessionsPresenter extends BasePresenter<SessionsView> {

    private final Lazy<SessionManager> sessionManager;
    private final TrackManager trackManager;
    private final EventManager eventManager;

    @Inject
    public SessionsPresenter(Lazy<SessionManager> sessionManager, EventManager eventManager, TrackManager trackManager) {
        this.sessionManager = sessionManager;
        this.trackManager = trackManager;
        this.eventManager = eventManager;
    }

    @Override
    public void attachView(SessionsView mvpView) {
        super.attachView(mvpView);
        eventManager.getEventParts()
                .flatMap(eventPart -> trackManager.getTracks().map(track -> Pair.create(track, eventPart)))
                .flatMap(pair -> sessionManager.get().getSessions(pair.first, pair.second.startTime, pair.second.endTime))
                .subscribe(session -> {

                });



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

    public void loadSession(int page) {

    }

}
