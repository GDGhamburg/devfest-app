package de.devfest.screens.speakerdetails;

import javax.inject.Inject;

import de.devfest.data.SpeakerManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SpeakerDetailsPresenter extends BasePresenter<SpeakerDetailsView> {

    private final SpeakerManager speakerManager;

    @Inject
    public SpeakerDetailsPresenter(SpeakerManager speakerManager) {
        this.speakerManager = speakerManager;
    }

    @Override
    public void attachView(SpeakerDetailsView mvpView) {
        super.attachView(mvpView);
        String speakerId = getView().getSpeakerId();
        untilDetach(speakerManager.getSpeaker(speakerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speaker -> {
                    getView().onSpeakerAvailable(speaker);
                }, error -> {
                    getView().onError(error);
                })
        );
    }
}
