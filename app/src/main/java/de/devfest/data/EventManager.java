package de.devfest.data;

import de.devfest.model.EventPart;
import rx.Observable;

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
}
