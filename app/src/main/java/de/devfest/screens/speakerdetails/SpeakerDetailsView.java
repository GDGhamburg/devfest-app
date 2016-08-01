package de.devfest.screens.speakerdetails;

import de.devfest.model.Speaker;
import de.devfest.mvpbase.MvpBase;

public interface SpeakerDetailsView extends MvpBase.View {

    String getSpeakerId();

    void onSpeakerAvailable(Speaker speaker);

    void onError(Throwable error);
}
