package de.devfest.data;

import de.devfest.model.Speaker;
import rx.Observable;

public interface SpeakerManager {
    /**
     * @return all speakers
     */
    Observable<Speaker> getSpeakers();

    Observable<Speaker> getSpeaker(String uid);

    @Deprecated
    Observable<Speaker> insertOrUpdate(Speaker speaker);
}
