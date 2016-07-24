package de.devfest.screens.speakerdetails;

import javax.inject.Inject;

import de.devfest.data.SpeakerManager;
import de.devfest.mvpbase.BasePresenter;

public class SpeakerDetailsPresenter extends BasePresenter<SpeakerDetailsView> {

    private final SpeakerManager speakerManager;

    @Inject
    public SpeakerDetailsPresenter(SpeakerManager speakerManager) {
        this.speakerManager = speakerManager;
    }

}
