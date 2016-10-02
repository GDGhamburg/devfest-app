package de.devfest.data;

import java.util.List;

import de.devfest.model.EventPart;
import rx.Single;

public interface EventManager {
    Single<List<EventPart>> getEventParts();
    Single<String> getEventName();
}
