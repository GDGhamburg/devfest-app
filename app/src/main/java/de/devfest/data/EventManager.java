package de.devfest.data;

import de.devfest.model.EventPart;
import rx.Single;

public interface EventManager {
    Single<EventPart> getEventParts();
    Single<String> getEventName();
}
