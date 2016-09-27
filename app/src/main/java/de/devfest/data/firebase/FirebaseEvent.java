package de.devfest.data.firebase;

import java.util.Map;

final class FirebaseEvent {
    public String name;
    public long startTime;
    public long endTime;
    public Map<String, FirebaseEventPart> eventPart;
}
