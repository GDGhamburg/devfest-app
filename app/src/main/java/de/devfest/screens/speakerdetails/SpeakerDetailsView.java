package de.devfest.screens.speakerdetails;

import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.mvpbase.MvpBase;

public interface SpeakerDetailsView extends MvpBase.AuthView {

    String getSpeakerId();

    void onSpeakerAvailable(Speaker speaker);

    void onSessionReceived(Session session, boolean scheduled);

    void onError(Throwable error);

    void openLink(String link);

    void showSessionDetails(String sessionId, String tag);
}
