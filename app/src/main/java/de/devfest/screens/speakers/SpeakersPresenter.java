package de.devfest.screens.speakers;

import javax.inject.Inject;

import de.devfest.data.DevFestManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SpeakersPresenter extends BasePresenter<SpeakersView> {

    private final DevFestManager devfestManager;

    @Inject
    public SpeakersPresenter(DevFestManager devfestManager) {
        this.devfestManager = devfestManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        untilOnPause(devfestManager.getSpeakers()
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
