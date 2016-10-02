package de.devfest.screens.speakerdetails;

import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.mvpbase.MvpBase;

public interface SpeakerDetailsView extends MvpBase.View {

    String getSpeakerId();

    void onSpeakerAvailable(Speaker speaker);
    void onSessionAvailable(Session session);

    void onError(Throwable error);

    void openLink(String link);

}
