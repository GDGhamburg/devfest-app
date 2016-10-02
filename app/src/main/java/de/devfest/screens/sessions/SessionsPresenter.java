package de.devfest.screens.sessions;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.EventManager;
import de.devfest.data.SessionManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This implementation is quite ugly, but since we were a bit short on time,
 * this will do it for now! I would love to have hot observables for data here
 */
public class SessionsPresenter extends BasePresenter<SessionsView> {

    private final Lazy<SessionManager> sessionManager;
    private final Lazy<EventManager> eventManager;


    @Inject
    public SessionsPresenter(Lazy<SessionManager> sessionManager, Lazy<EventManager> eventManager) {
        this.sessionManager = sessionManager;
        this.eventManager = eventManager;
    }

    @Override
    public void attachView(SessionsView mvpView) {
        super.attachView(mvpView);
        untilDetach(
                eventManager.get().getEventParts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(eventPart -> {
                            getView().onEventPartReceived(eventPart);
                        }, error -> {
                            getView().onError(error);
                        })
        );

    }

}
