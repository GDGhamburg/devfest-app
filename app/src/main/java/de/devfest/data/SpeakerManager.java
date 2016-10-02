package de.devfest.data;

import de.devfest.model.Speaker;
import rx.Observable;

public interface SpeakerManager {
    /**
     * @return a hot observable which returns all available speakers.
     */
    Observable<Speaker> getSpeakers();

    /**
     * @param uid of the speaker you want to receive
     * @return a hot observable returning a single speaker
     */
    Observable<Speaker> getSpeaker(String uid);
}
