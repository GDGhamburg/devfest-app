package de.devfest.data.firebase;

import java.util.Map;

final class FirebaseSession {
    public String id;
    public String title;
    public String description;
    public long datetime;
    public long duration;
    public String track;
    public String stage;
    public String language;
    public boolean isScheduable;
    public String tags;

    public Map<String, String> speakers;

    FirebaseSession() {
    }
}
