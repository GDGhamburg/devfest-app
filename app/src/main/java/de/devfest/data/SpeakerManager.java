package de.devfest.data;

import de.devfest.model.Speaker;
import rx.Observable;
import rx.Single;

public interface SpeakerManager {
    /**
     * @return all speakers
     */
    Observable<Speaker> getSpeakers();

    Single<Speaker> getSpeaker(String uid);

    @Deprecated
    Observable<Speaker> insertOrUpdate(Speaker speaker);
}
