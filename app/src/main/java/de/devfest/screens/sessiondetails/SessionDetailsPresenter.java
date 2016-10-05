package de.devfest.screens.sessiondetails;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.mvpbase.BasePresenter;

public class SessionDetailsPresenter extends BasePresenter<SessionDetailsView> {

    private final Lazy<SpeakerManager> speakerManager;
    private final Lazy<SessionManager> sessionManager;

    @Inject
    public SessionDetailsPresenter(Lazy<SpeakerManager> speakerManager, Lazy<SessionManager> sessionManager) {
        this.speakerManager = speakerManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public void attachView(SessionDetailsView mvpView) {
        super.attachView(mvpView);
        String sessionId = getView().getSessionId();
//        untilDetach(sessionManager.get().getSession(sessionId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(speaker -> getView().onSessionAvailable(speaker))
//                .observeOn(Schedulers.io())
//                .flatMap(session -> speakerManager.get().getSpeakers(session.speaker))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        session -> {
//                            getView().onSessionAvailable(session);
//                        }, error -> {
//                            getView().onError(error);
//                        }));
    }
}
