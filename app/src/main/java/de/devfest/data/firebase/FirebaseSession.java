package de.devfest.data.firebase;

import java.util.HashMap;

public final class FirebaseSession {
    public String title;
    public String description;
    public long datetime;
    public long duration;
    public String track;
    public String stage;
    public String language;
    public HashMap<String, String> speakers;

    private FirebaseSession(Builder builder) {
        title = builder.title;
        description = builder.description;
        datetime = builder.datetime;
        duration = builder.duration;
        track = builder.track;
        stage = builder.stage;
        language = builder.language;
        speakers = builder.speakers;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String title;
        private String description;
        private long datetime;
        private long duration;
        private String track;
        private String stage;
        private String language;
        private HashMap<String, String> speakers;

        private Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder datetime(long val) {
            datetime = val;
            return this;
        }

        public Builder duration(long val) {
            duration = val;
            return this;
        }

        public Builder track(String val) {
            track = val;
            return this;
        }

        public Builder stage(String val) {
            stage = val;
            return this;
        }

        public Builder language(String val) {
            language = val;
            return this;
        }

        public Builder speakers(HashMap<String, String> val) {
            speakers = val;
            return this;
        }

        public FirebaseSession build() {
            return new FirebaseSession(this);
        }
    }
}
