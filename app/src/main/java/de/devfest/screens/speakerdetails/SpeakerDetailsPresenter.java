package de.devfest.screens.speakerdetails;

import android.support.v4.util.Pair;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.model.SocialLink;
import de.devfest.mvpbase.BasePresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SpeakerDetailsPresenter extends BasePresenter<SpeakerDetailsView> {

    private final Lazy<SpeakerManager> speakerManager;
    private final Lazy<SessionManager> sessionManager;

    @Inject
    public SpeakerDetailsPresenter(Lazy<SpeakerManager> speakerManager, Lazy<SessionManager> sessionManager) {
        this.speakerManager = speakerManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public void attachView(SpeakerDetailsView mvpView) {
        super.attachView(mvpView);
        String speakerId = getView().getSpeakerId();
        untilDetach(speakerManager.get().getSpeaker(speakerId)
                .flatMapObservable(speaker -> Observable.zip(
                        Observable.just(speaker),
                        Observable.from(speaker.sessions)
                                .flatMap(id -> sessionManager.get().getSessionById(id).toObservable())
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

    public void onLinkClick(SocialLink link) {
        getView().openLink(link.link);
    }
}
