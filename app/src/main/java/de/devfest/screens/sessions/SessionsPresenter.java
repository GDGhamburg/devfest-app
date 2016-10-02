package de.devfest.screens.sessions;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.EventManager;
import de.devfest.data.SessionManager;
import de.devfest.data.TrackManager;
import de.devfest.model.EventPart;
import de.devfest.model.Track;
import de.devfest.mvpbase.BasePresenter;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * This implementation is quite ugly, but since we were a bit short on time,
 * this will do it for now! I would love to have hot observables for data here
 */
public class SessionsPresenter extends BasePresenter<SessionsView> {

    private final Lazy<SessionManager> sessionManager;
    private final Lazy<TrackManager> trackManager;
    private final Lazy<EventManager> eventManager;


    /**
     * ordered event parts
     */
    private List<EventPart> parts;

    /**
     * ordered tracks
     */
    private List<Track> tracks;

    @Inject
    public SessionsPresenter(Lazy<SessionManager> sessionManager, Lazy<EventManager> eventManager, Lazy<TrackManager> trackManager) {
        this.sessionManager = sessionManager;
        this.trackManager = trackManager;
        this.eventManager = eventManager;
    }

    @Override
    public void attachView(SessionsView mvpView) {
        super.attachView(mvpView);
        Observable.zip(
                trackManager.get().getTracks().doOnSuccess(l -> Log.e("", "success")).toObservable(),
                eventManager.get().getEventParts().toObservable(),
                new Func2<List<Track>, List<EventPart>, Integer>() {
                    @Override
                    public Integer call(List<Track> t, List<EventPart> p) {
                        tracks = t;
                        parts = p;
                        return tracks.size() * parts.size();
                    }
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((size) -> {
                    getView().finishedInitializaiton(size);
                });



    }

    public void loadSession(int page) {
        sessionManager.get().getSessions(
                tracks.get(page % tracks.size()),
                parts.get(page / parts.size())
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> getView().onSessionsReceived(page, list),
                        error -> getView().onError(error));
    }

}
