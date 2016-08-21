package de.devfest.screens.speakerdetails;

import java.util.List;

import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.mvpbase.MvpBase;

public interface SpeakerDetailsView extends MvpBase.View {

    String getSpeakerId();

    void onSpeakerAvailable(Speaker speaker, List<Session> sessions);

    void onError(Throwable error);
}
