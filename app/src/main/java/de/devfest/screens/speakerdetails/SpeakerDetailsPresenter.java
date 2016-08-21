package de.devfest.screens.speakerdetails;

import android.support.v4.util.Pair;

import javax.inject.Inject;

import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.mvpbase.BasePresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SpeakerDetailsPresenter extends BasePresenter<SpeakerDetailsView> {

    private final SpeakerManager speakerManager;
    private final SessionManager sessionManager;

    @Inject
    public SpeakerDetailsPresenter(SpeakerManager speakerManager, SessionManager sessionManager) {
        this.speakerManager = speakerManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public void attachView(SpeakerDetailsView mvpView) {
        super.attachView(mvpView);
        String speakerId = getView().getSpeakerId();
        untilDetach(speakerManager.getSpeaker(speakerId)
                .flatMap(speaker -> Observable.zip(
                        Observable.just(speaker),
                        Observable.from(speaker.sessions)
                                .flatMap(sessionManager::getSessionById)
                                .toList(),
                        Pair::create))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speaker -> {
                    getView().onSpeakerAvailable(speaker.first, speaker.second);
                }, error -> {
                    getView().onError(error);
                })
        );
    }
}
