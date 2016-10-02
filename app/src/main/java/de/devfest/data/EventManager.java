package de.devfest.data;

import de.devfest.model.EventPart;
import rx.Observable;
import rx.Single;

public interface EventManager {
    /**
     * @return a hot observable emmiting all eventparts
     */
    Observable<EventPart> getEventParts();

    /**
     * @param id of the part you want to receive
     * @return a hot observable emmiting eventparts by the given id
     */
    Observable<EventPart> getEventPart(String id);

    /**
     * @return if the event is currently available for non admin users
     */
    Single<Boolean> isActive();
}
