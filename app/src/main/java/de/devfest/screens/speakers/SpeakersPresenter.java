package de.devfest.screens.speakers;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SpeakerManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SpeakersPresenter extends BasePresenter<SpeakersView> {

    private final Lazy<SpeakerManager> speakerManager;

    @Inject
    public SpeakersPresenter(Lazy<SpeakerManager> speakerManager) {
        this.speakerManager = speakerManager;
    }

    @Override
    public void attachView(SpeakersView mvpView) {
        super.attachView(mvpView);
        untilDetach(speakerManager.get().getSpeakers()
                .onBackpressureBuffer()
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
