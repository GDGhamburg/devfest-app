package de.devfest.screens.speakers;

import android.support.annotation.NonNull;

import de.devfest.model.Speaker;
import de.devfest.mvpbase.MvpBase;

public interface SpeakersView extends MvpBase.View {
    /**
     * Will be called when a speaker is available. This method will be called fast multiple times and can contain speakers,
     * which are shown already (on an update i.e.)
     *
     * @param speaker Speaker you want to show
     */
    void onSpeakerAvailable(@NonNull Speaker speaker);

    /**
     * Called when an error appears
     *
     * @param error show a message to the user
     */
    void onError(Throwable error);
}
