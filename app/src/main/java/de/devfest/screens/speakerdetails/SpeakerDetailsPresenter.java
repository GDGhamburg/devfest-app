package de.devfest.screens.speakerdetails;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.model.SocialLink;
import de.devfest.mvpbase.BasePresenter;
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(speaker -> getView().onSpeakerAvailable(speaker))
                .observeOn(Schedulers.io())
                .flatMap(speaker -> sessionManager.get().getSession(speaker.sessions))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        session -> {
                            getView().onSessionAvailable(session);
                        }, error -> {
                            getView().onError(error);
                        }));
    }

    public void onLinkClick(SocialLink link) {
        getView().openLink(link.link);
    }
}
